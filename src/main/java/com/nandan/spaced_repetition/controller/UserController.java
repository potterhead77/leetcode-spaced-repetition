package com.nandan.spaced_repetition.controller;

import com.nandan.spaced_repetition.dto.UserRecentSubmissionsResponse;
import com.nandan.spaced_repetition.service.LeetCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

@RestController
@RequestMapping("/api/v1/users/{username}")
@RequiredArgsConstructor
public class UserController {

    private final LeetCodeService leetCodeService;

    /**
     * Get the most recent accepted submissions for a user.
     * Useful for verifying that the API allows you to see what you just solved.
     */
    @GetMapping("/recentUserSubmissions/{limit}")
    public ResponseEntity<UserRecentSubmissionsResponse.DataNode> getUserRecentSubmissions(@PathVariable String username, @PathVariable int limit) {
        Logger.info("getUserRecentSubmissions() method called with username :: {}", username);
        return leetCodeService.getUserRecentSubmissions(username, limit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}