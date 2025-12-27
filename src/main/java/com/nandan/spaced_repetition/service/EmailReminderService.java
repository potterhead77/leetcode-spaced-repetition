package com.nandan.spaced_repetition.service;

import com.nandan.spaced_repetition.dto.SpacedRepetitionDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailReminderService {

    private final JavaMailSender mailSender;
    private final SpacedRepetitionService srService;

    /**
     * Automatically sends an email every day at 9:00 AM.
     * The Cron expression format: [second] [minute] [hour] [day] [month] [weekday]
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminder() {
        String username = "nandansanjay35";

        // Fetch questions that are due or overdue based on SM-2 logic
        List<SpacedRepetitionDTOs.DueQuestionResponse> dueQuestions = srService.getDueQuestions(username);

        if (dueQuestions.isEmpty()) {
            Logger.info("No LeetCode questions due today for {}. Skipping email.", username);
            return;
        }

        StringBuilder body = new StringBuilder();
        body.append("Good morning! 🧠\n\n");
        body.append("You have ").append(dueQuestions.size()).append(" LeetCode problems to review today:\n\n");

        for (SpacedRepetitionDTOs.DueQuestionResponse q : dueQuestions) {
            body.append("• ").append(q.getTitleSlug()).append("\n")
                    .append("  Practice here: ").append(q.getLink()).append("\n\n");
        }

        body.append("Consistency is the secret to mastering algorithms. Happy coding!");

        sendEmail("nandansanjay35@gmail.com", "Action Required: " + dueQuestions.size() + " LeetCode Reviews Due", body.toString());
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            Logger.info("Daily reminder email sent successfully to {}", to);
        } catch (Exception e) {
            Logger.error("Failed to send email to {}. Error: {}", to, e.getMessage());
        }
    }
}