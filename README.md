# LeetCode Spaced Repetition System 🧠

A specialized backend service that uses the **SM-2 Spaced Repetition Algorithm** to optimize algorithm retention.

Unlike standard practice tools, this system tracks *when* you are about to forget a problem and schedules a review at the optimal time.

## 🔥 Key Features
- **Smart Scheduling:** Automatically calculates review intervals based on user performance (Easy/Medium/Hard).
- **Automated Sync:** Polls LeetCode every 5 minutes to detect new submissions.
- **Immediate Feedback Loop:** Custom REST API to submit "difficulty ratings" after every review.
- **Focus Mode:** Filters out the noise (contests, badges) and focuses strictly on daily reviews.

## 🛠️ Tech Stack
- **Core:** Java 17, Spring Boot 3
- **Database:** SQLite (Self-contained, no setup required)
- **Algorithm:** SuperMemo-2 (SM-2)
- **Docs:** OpenAPI / Swagger UI

## 🚀 Getting Started
1. Run the app: `./mvnw spring-boot:run`
2. Access Swagger UI: `http://localhost:8080/swagger-ui.html`
3. Check your daily reviews: `GET /api/v1/sr/{username}/due`