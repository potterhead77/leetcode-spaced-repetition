package com.nandan.spaced_repetition.service;

import com.nandan.spaced_repetition.client.LeetCodeClient;
import com.nandan.spaced_repetition.dto.SpacedRepetitionDTOs;
import com.nandan.spaced_repetition.dto.UserRecentSubmissionsResponse;
import com.nandan.spaced_repetition.entity.UserSpacedRepetitionEntity;
import com.nandan.spaced_repetition.repository.UserSpacedRepetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nandan.spaced_repetition.constants.Constants.PROBLEM_URL_PREFIX;

@Service
@RequiredArgsConstructor
public class SpacedRepetitionService {

    private final UserSpacedRepetitionRepository srRepository;
    private final LeetCodeClient leetCodeClient;

    /**
     * Syncs recent AC submissions from LeetCode to the SR database.
     * Call this periodically or on user login.
     */
    public void syncUserSubmissions(String username) {
        Logger.info("Syncing submissions for SR: {}", username);
        // Fetch last 50 submissions to catch up
        var response = leetCodeClient.fetchUserRecentSubmissions(username, 50);

        if (response != null && response.getData() != null) {
            List<UserRecentSubmissionsResponse.Submission> submissions = response.getData().getRecentAcSubmissionList();
            for (UserRecentSubmissionsResponse.Submission sub : submissions) {
                // We only care if it's "Accepted" (usually implied by recentAcSubmissionList)
                // Add to DB if not exists
                if (srRepository.findByUsernameAndTitleSlug(username, sub.getTitleSlug()).isEmpty()) {
                    UserSpacedRepetitionEntity newEntry = new UserSpacedRepetitionEntity(username, sub.getTitleSlug());
                    srRepository.save(newEntry);
                    Logger.info("Added new SR entry for: {}", sub.getTitleSlug());
                }
            }
        }
    }

    /**
     * Get all questions due for review today (or overdue).
     */
    public List<SpacedRepetitionDTOs.DueQuestionResponse> getDueQuestions(String username) {
        // First sync to make sure we have the latest
        try {
            syncUserSubmissions(username);
        } catch (Exception e) {
            Logger.warn("Failed to sync submissions during getDueQuestions: {}", e.getMessage());
        }

        return srRepository.findByUsernameAndNextReviewDateBefore(username, LocalDateTime.now())
                .stream()
                .map(entity -> SpacedRepetitionDTOs.DueQuestionResponse.builder()
                        .titleSlug(entity.getTitleSlug())
                        .link(PROBLEM_URL_PREFIX + entity.getTitleSlug() + "/")
                        .nextReviewDate(entity.getNextReviewDate())
                        .intervalDays(entity.getIntervalDays())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Process a review (User solved it again and gave feedback quality 0-5).
     * Implements SM-2 Algorithm.
     */
    public void submitReview(String username, String titleSlug, int quality) {
        Optional<UserSpacedRepetitionEntity> entryOpt = srRepository.findByUsernameAndTitleSlug(username, titleSlug);

        if (entryOpt.isPresent()) {
            UserSpacedRepetitionEntity card = entryOpt.get();
            calculateNextInterval(card, quality);
            srRepository.save(card);
            Logger.info("Updated SR for {} - Next review in {} days", titleSlug, card.getIntervalDays());
        } else {
            // Should not happen if synced, but handle gracefully
            UserSpacedRepetitionEntity newCard = new UserSpacedRepetitionEntity(username, titleSlug);
            calculateNextInterval(newCard, quality);
            srRepository.save(newCard);
        }
    }

    private void calculateNextInterval(UserSpacedRepetitionEntity card, int quality) {
        if (quality < 3) {
            // If they forgot, reset repetitions and interval
            card.setRepetitions(0);
            card.setIntervalDays(1);
        } else {
            // Update Ease Factor
            double newEase = card.getEaseFactor() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
            if (newEase < 1.3) newEase = 1.3; // Minimum ease cap
            card.setEaseFactor(newEase);

            // Update Interval
            if (card.getRepetitions() == 0) {
                card.setIntervalDays(1);
            } else if (card.getRepetitions() == 1) {
                card.setIntervalDays(6);
            } else {
                card.setIntervalDays((int) Math.round(card.getIntervalDays() * card.getEaseFactor()));
            }
            card.setRepetitions(card.getRepetitions() + 1);
        }

        card.setLastSolvedDate(LocalDateTime.now());
        card.setNextReviewDate(LocalDateTime.now().plusDays(card.getIntervalDays()));
    }

    /**
     * Resets progress for a specific user to allow immediate review.
     */
    public void resetProgressForUser(String username) {
        List<UserSpacedRepetitionEntity> allEntries = srRepository.findAll().stream()
                .filter(e -> e.getUsername().equals(username))
                .collect(Collectors.toList());

        for (UserSpacedRepetitionEntity entry : allEntries) {
            // Move the review date to the past
            entry.setNextReviewDate(LocalDateTime.now().minusDays(1));
            // Optional: Reset progress too
            entry.setIntervalDays(0);
            entry.setRepetitions(0);
        }
        srRepository.saveAll(allEntries);
        Logger.info("Reset SR progress for user: {}", username);
    }
}