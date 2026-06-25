# LifeSync Backend 🔄

> Personal Life OS — built with Spring Boot Microservices, Java 21, PostgreSQL, Kafka, and Eureka.

## Services
| Service | Port | Responsibility |
|---|---|---|
| discovery-service | 8761 | Eureka service registry |
| config-service | 8888 | Centralized configuration |
| api-gateway | 8080 | Routing + JWT validation |
| user-service | 8081 | Auth + JWT |
| goal-service | 8082 | Goals + milestones |
| habit-service | 8083 | Habits + streaks |
| job-tracker-service | 8084 | Job applications |
| ai-coach-service | 8085 | Groq AI daily briefing |
| notification-service | 8086 | Kafka + email alerts |

## Tech Stack
Spring Boot 3.3 · Spring Cloud · PostgreSQL · Apache Kafka · Docker · React

## Status
🚧 Under active development — Sprint 0 complete
