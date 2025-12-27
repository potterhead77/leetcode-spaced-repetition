package com.nandan.spaced_repetition.controller;

import com.nandan.spaced_repetition.dto.SpacedRepetitionDTOs;
import com.nandan.spaced_repetition.service.SpacedRepetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sr")
@RequiredArgsConstructor
public class SpacedRepetitionController {

    private final SpacedRepetitionService srService;

    @GetMapping("/{username}/due")
    public ResponseEntity<List<SpacedRepetitionDTOs.DueQuestionResponse>> getDueReviews(@PathVariable String username) {
        return ResponseEntity.ok(srService.getDueQuestions(username));
    }

    @PostMapping("/{username}/review")
    public ResponseEntity<String> submitReview(
            @PathVariable String username,
            @RequestBody SpacedRepetitionDTOs.ReviewRequest request) {

        if (request.getQuality() < 0 || request.getQuality() > 5) {
            return ResponseEntity.badRequest().body("Quality must be between 0 (hard/forgot) and 5 (perfect).");
        }

        srService.submitReview(username, request.getTitleSlug(), request.getQuality());
        return ResponseEntity.ok("Review submitted successfully.");
    }

    /**
     * Resets the spaced repetition progress for a user.
     * Useful for testing or when a user wants to restart their review cycle.
     */
    @PostMapping("/{username}/reset")
    public ResponseEntity<String> resetProgress(@PathVariable String username) {
        srService.resetProgressForUser(username);
        return ResponseEntity.ok("User progress has been reset. All questions should now be due.");
    }
}