package com.nandan.spaced_repetition.dto;

import lombok.Data;

@Data
public class DailyCodingChallengeResponse {
    public DataNode data;

    @Data
    public static class DataNode {
        public ActiveDailyCodingChallengeQuestion activeDailyCodingChallengeQuestion;
    }

    @Data
    public static class ActiveDailyCodingChallengeQuestion {
        private String date;
        private String link;
        private Question question;
    }

    @Data
    public static class Question {
        private String title;
        private String titleSlug;
    }

}
