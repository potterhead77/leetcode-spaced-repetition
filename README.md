LeetCode Spaced Repetition (SR) Engine
A full-stack engineered Spring Boot backend designed to optimize algorithm retention using the SM-2 Spaced Repetition algorithm. The system autonomously tracks your LeetCode activity, persists learning states in a PostgreSQL database, calculates optimal review intervals, and proactively notifies you via email when it's time to practice.

🚀 Key Features
SM-2 Algorithm Implementation: Custom implementation of the SuperMemo-2 algorithm to calculate the "Next Review Date" based on performance quality (0-5) and streak history.

LeetCode Sync Engine: Automated periodic polling of the LeetCode GraphQL API to synchronize your latest "Accepted" submissions and metadata.

Automated Email Reminders: A quartz-scheduled service that compiles due items and dispatches a structured review list daily at 9:00 AM via Gmail SMTP.

Cloud-Native Architecture: Containerized using Docker and deployed on Railway for high availability and zero-downtime updates.

Resilience : Integrated Resilience4j circuit breakers handle external API limits gracefully.

CI/CD Pipeline: Fully automated pipeline using GitHub Actions for continuous integration (Build & Test) and Railway for continuous deployment (CD).

🛠️ Tech Stack
Core Framework: Spring Boot 3.x (Java 17/24)

Database: PostgreSQL 14+ (Production), H2 (Testing)

Containerization: Docker & Docker Compose

Cloud Platform: Railway

Communication: JavaMailSender (SMTP), LeetCode GraphQL API

CI/CD: GitHub Actions (Maven Build/Test), Railway Webhooks

Monitoring: Spring Actuator & Micrometer

⚙️ Setup & Configuration
Prerequisites
Java 17+

Docker Desktop (for local database)

Maven

Gmail App Password (for email alerts)
