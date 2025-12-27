LeetCode Spaced Repetition (SR) Engine
A Spring Boot-powered backend designed to optimize algorithm retention using the SM-2 Spaced Repetition algorithm. The system tracks your LeetCode activity, calculates optimal review intervals, and proactively notifies you when it's time to practice.

🚀 Key Features
SM-2 Algorithm Implementation: Automatically calculates next review dates based on your performance (Quality 0-5).

LeetCode Sync: Periodic polling of the LeetCode GraphQL API to pull your latest "Accepted" submissions.

Automated Email Reminders: A scheduled service that sends daily review lists at 9:00 AM via Gmail SMTP.

Rate Limiting & Resilience: Integrated Bucket4j and Resilience4j to handle LeetCode API limits gracefully.

CI/CD Pipeline: Fully automated build, security scan (CodeQL), and code quality (Qodana) checks via GitHub Actions.

🛠️ Tech Stack
Framework: Spring Boot 3.x

Database: SQLite (Zero-config, local storage)

Communication: JavaMailSender (SMTP), LeetCode GraphQL API

Automation: GitHub Actions (CI/CD)

Monitoring: Spring Actuator & Micrometer

⚙️ Setup & Configuration
Prerequisites
Java 17+

Maven

Gmail App Password (for reminders)

Configuration
Update src/main/resources/application.yml:

YAML

spring:
  mail:
    username: your-email@gmail.com
    password: ${GMAIL_APP_PASSWORD} # 16-character google app password
📈 API Usage
Sync Submissions
POST /api/v1/sr/{username}/sync

Manually triggers a pull from LeetCode.

Submit a Review
POST /api/v1/sr/review

Payload:

JSON

{
  "username": "nandansanjay35",
  "titleSlug": "two-sum",
  "quality": 5 
}
Quality Scale: 0 (forgot completely) to 5 (perfect recall).

🤖 CI/CD Workflows
This project maintains high code standards through automated GitHub Actions:

Build: Compiles and verifies the project on every push.

Security: Scans for vulnerabilities using CodeQL.

Quality: Static analysis provided by JetBrains Qodana.
