resource "aws_ecs_cluster" "main" {
  name = "habit-service"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_security_group" "ecs_tasks" {
  name   = "habit-service-tasks"
  vpc_id = module.vpc.vpc_id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_cloudwatch_log_group" "habit_service" {
  name              = "/ecs/habit-service"
  retention_in_days = 14
}

resource "aws_ecs_task_definition" "habit_service" {
  family                   = "habit-service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn
  task_role_arn            = aws_iam_role.ecs_task.arn

  container_definitions = jsonencode([{
    name  = "habit-service"
    image = "${aws_ecr_repository.habit_service.repository_url}:${var.image_tag}"

    portMappings = [{ containerPort = 8080, protocol = "tcp" }]

    environment = [
      { name = "SPRING_PROFILES_ACTIVE", value = "default" },
      { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_db_instance.main.endpoint}/habits" },
      { name = "SPRING_DATASOURCE_USERNAME", value = "habits" },
      { name = "JWT_ISSUER", value = "habit-tracker-${var.environment}" }
    ]

    secrets = [
      { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_secretsmanager_secret.db_password.arn },
      { name = "JWT_SECRET", valueFrom = aws_secretsmanager_secret.jwt_secret.arn }
    ]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.habit_service.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "habit-service"
      }
    }

    healthCheck = {
      command     = ["CMD-SHELL", "wget -qO- http://localhost:8080/actuator/health || exit 1"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60
    }
  }])
}

resource "aws_ecs_service" "habit_service" {
  name            = "habit-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.habit_service.arn
  desired_count   = 2
  launch_type     = "FARGATE"

  deployment_minimum_healthy_percent = 100
  deployment_maximum_percent         = 200

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.ecs_tasks.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.habit_service.arn
    container_name   = "habit-service"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.https]

  lifecycle {
    ignore_changes = [task_definition] # CI updates this; Terraform shouldn't fight it.
  }
}
