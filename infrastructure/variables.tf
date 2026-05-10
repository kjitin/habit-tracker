variable "aws_region" {
  type    = string
  default = "eu-west-2"
}

variable "environment" {
  type    = string
  default = "prod"
}

variable "domain_name" {
  type        = string
  description = "Domain name for the service. Must have an ACM cert in this region."
}

variable "acm_certificate_arn" {
  type        = string
  description = "ARN of an ACM certificate covering domain_name."
}

variable "image_tag" {
  type        = string
  default     = "latest"
  description = "Tag of the container image to deploy."
}

variable "db_instance_class" {
  type    = string
  default = "db.t4g.micro"
}
