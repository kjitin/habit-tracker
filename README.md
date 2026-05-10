# Habit Tracker

Reference repository for the book *Spring Boot Monorepo on AWS: Build and Ship Your First Real Backend*.

## What this is

A backend service for tracking daily habits. JWT auth, Postgres, SQS event publishing, deployable to AWS via Terraform. Built as a vehicle for teaching how to assemble a real production-shape Spring Boot service end-to-end.

## Layout

```
.
├── platform-libs/
│   ├── common-web/          # cross-cutting web stuff: error handling, request IDs
│   └── common-security/     # JWT issuance/verification, security filters, principal
├── services/
│   └── habit-service/       # the actual application
├── infrastructure/          # Terraform for AWS deploy
├── docker-compose.yml       # local stack
└── settings.gradle.kts
```

## Running locally

The fast path — no database needed:

```
SPRING_PROFILES_ACTIVE=dev-local ./gradlew :services:habit-service:bootRun
```

The full path with Postgres:

```
docker compose up -d postgres
./gradlew :services:habit-service:bootRun
```

Or everything in containers:

```
docker compose up --build
```

## Try it

```
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"me@example.com","password":"hunter12345","displayName":"Me"}'
# -> { "token": "eyJ..." }

# Create a habit
TOKEN=<paste token>
curl -X POST http://localhost:8080/api/v1/habits \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Drink water","frequency":"DAILY"}'

# List habits
TOKEN=<paste token>
curl http://localhost:8080/api/v1/habits \
  -H "Authorization: Bearer $TOKEN"
```

## Gradle wrapper

This repository ships build files but not the Gradle wrapper binaries (the `gradlew` script and `gradle/wrapper/` directory). To generate them, run once with a system Gradle (8.10+):

```
gradle wrapper --gradle-version 8.10
```

Then commit the generated `gradlew`, `gradlew.bat`, and `gradle/wrapper/`.

