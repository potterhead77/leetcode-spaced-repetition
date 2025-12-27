package com.nandan.spaced_repetition.controller;

import com.nandan.spaced_repetition.service.EmailReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailReminderService emailReminderService;

    @PostMapping("/send-reminder")
    public String triggerManualEmail() {
        emailReminderService.sendDailyReminder();
        return "Manual email trigger sent! Check your IntelliJ console for logs and your inbox.";
    }
}