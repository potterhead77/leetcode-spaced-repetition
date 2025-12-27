package com.nandan.spaced_repetition.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nandan.spaced_repetition.enums.questions.FilterOperator;
import com.nandan.spaced_repetition.enums.questions.QuestionDifficulty;
import com.nandan.spaced_repetition.enums.questions.SortField;
import com.nandan.spaced_repetition.enums.questions.SortOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionSearchRequest {
    private int skip;
    private int limit;
    private String categorySlug;
    private String searchKeyword;
    private SortingCriteria sortBy;
    private FilterCriteria filters;


    @Data
    @Builder
    public static class SortingCriteria {
        private SortField sortField;
        private SortOrder sortOrder;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FilterCriteria {
        private String filterCombineType;
        private DifficultyFilter difficultyFilter;
        private LanguageFilter languageFilter;
        private TopicFilter topicFilter;
        private RangeFilter acceptanceFilter;
        private RangeFilter frontendIdFilter;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DifficultyFilter {
        private List<QuestionDifficulty> difficulties;
        private FilterOperator operator;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LanguageFilter {
        private List<String> languageSlugs;
        private FilterOperator operator;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TopicFilter {
        private List<String> topicSlugs;
        private FilterOperator operator;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RangeFilter {
        private double rangeLeft;
        private double rangeRight;
    }
}
