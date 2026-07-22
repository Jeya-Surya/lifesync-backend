<div align="center">

# 🔄 LifeSync Backend

### Personal Life OS — Microservices Backend

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.7-black?style=flat-square&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)](https://www.docker.com/)

</div>

---

## 📌 Overview

LifeSync is a **Personal Life OS** built with Spring Boot Microservices. It helps users track goals, build habits, manage job applications, and receive AI-powered daily coaching — all in one place.

> Built by [Jeya Surya](https://github.com/Jeya-Surya) as a full-stack portfolio project demonstrating production-grade microservices architecture.

---

## 🏗️ Architecture

```text
React Frontend (Vite + Tailwind)
↓
API Gateway (Spring Cloud Gateway) — port 8080
↓
Eureka Service Registry — port 8761
↓
┌─────────────────────────────────────────────┐
│ user-service │ goal-service │
│ habit-service │ job-tracker-service │
│ ai-coach-service │ notification-service │
└─────────────────────────────────────────────┘
↓
PostgreSQL (5 isolated databases) + Apache Kafka
```
---

## 🧩 Microservices

| Service | Port | Responsibility |
|---|---|---|
| `discovery-service` | 8761 | Eureka service registry |
| `api-gateway` | 8080 | Routing, JWT validation, CORS |
| `user-service` | 8081 | Registration, login, JWT auth |
| `goal-service` | 8082 | Goals, milestones, progress tracking |
| `habit-service` | 8083 | Habits, daily check-ins, streak calculation |
| `job-tracker-service` | 8084 | Job applications, interview rounds, stats |
| `ai-coach-service` | 8085 | Groq AI daily briefing and insights |
| `notification-service` | 8086 | Kafka consumer, async notifications |

---

## ⚙️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.3 |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Eureka (Spring Cloud Netflix) |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA + Hibernate |
| Messaging | Apache Kafka |
| AI Integration | Groq API (llama-3.3-70b-versatile) |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven (multi-module) |

---

## 🚀 Running Locally

### Prerequisites
- Java 21
- Maven
- Docker Desktop

### Option 1 — Docker Compose (Recommended)

```bash
# Clone the repo
git clone https://github.com/Jeya-Surya/lifesync-backend.git
cd lifesync-backend

# Create .env file
echo "GROQ_API_KEY=your_groq_api_key" > .env

# Build all services
./mvnw clean package -DskipTests

# Start everything
docker-compose up --build

```

All services start automatically. Visit `http://localhost:8761` for Eureka dashboard.

### Option 2 — Manual (IntelliJ)

Start services in this order:
1. `DiscoveryServiceApplication` (8761)
2. `ApiGatewayApplication` (8080)
3. `UserServiceApplication` (8081)
4. `GoalServiceApplication` (8082)
5. `HabitServiceApplication` (8083)
6. `JobTrackerServiceApplication` (8084)
7. `AiCoachServiceApplication` (8085)
8. `NotificationServiceApplication` (8086)

---

## 🔑 Key Concepts Demonstrated

- **Microservices Architecture** — 8 independent services with single responsibility
- **Database per Service** — 5 isolated PostgreSQL databases
- **JWT Authentication** — stateless auth shared across all services
- **API Gateway Pattern** — single entry point with routing and CORS
- **Service Discovery** — Eureka registry, no hardcoded URLs
- **Async Messaging** — Kafka producers and consumers
- **AI Integration** — Groq API for personalized coaching
- **Docker Compose** — full stack containerization

---

## 📡 API Endpoints

### Auth
```text
POST /api/auth/register
POST /api/auth/login
GET /api/users/me
```

---

### Goals
```text
POST /api/goals
GET /api/goals
GET /api/goals/{id}
PUT /api/goals/{id}
DELETE /api/goals/{id}
POST /api/goals/{id}/milestones
PATCH /api/goals/{id}/milestones/{mId}/complete
DELETE /api/goals/{id}/milestones/{mId}
```

### Habits
```text
POST /api/habits
GET /api/habits
POST /api/habits/{id}/checkin
GET /api/habits/{id}/streak
GET /api/habits/{id}/logs
```

### Job Tracker
```text
POST /api/jobs
GET /api/jobs
PATCH /api/jobs/{id}/status
POST /api/jobs/{id}/rounds
GET /api/jobs/stats
```

### AI Coach
```text
GET /api/coach/briefing
```

### Notifications
```text
GET /api/notifications
GET /api/notifications/unread
GET /api/notifications/count
PUT /api/notifications/read-all
```

---

## 👨‍💻 Author

**Jeya Surya** 
Bachelor of Technology in Computer Science and Business Systems

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/jeyasuryads)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat-square&logo=github)](https://github.com/Jeya-Surya)
