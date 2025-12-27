package com.nandan.spaced_repetition.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

public class SpacedRepetitionDTOs {

    @Data
    public static class ReviewRequest {
        private String titleSlug;
        private int quality; // 0 (forgot) to 5 (perfect)
    }

    @Data
    @Builder
    public static class DueQuestionResponse {
        private String titleSlug;
        private String link;
        private LocalDateTime nextReviewDate;
        private int intervalDays;
    }
}