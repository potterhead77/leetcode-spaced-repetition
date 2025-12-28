package com.nandan.spaced_repetition.client;

import com.nandan.spaced_repetition.dto.QuestionListResponse;
import com.nandan.spaced_repetition.dto.UserRecentSubmissionsResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nandan.spaced_repetition.graphql.GraphQlQueries.*;

@Service
public class LeetCodeClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${leetcode.graphql.url}")
    private String leetcodeApiUrl;

    /**
     * Fetches all questions from LeetCode with a high limit.
     * Used by the Scheduler to keep the local DB in sync.
     */
    @RateLimiter(name = "leetcode-api")
    public QuestionListResponse fetchAllQuestions(Boolean forAcRateSync) {
        HttpHeaders headers = new HttpHeaders();
        setHeader(headers);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", forAcRateSync ? FETCH_QUESTIONS_FOR_AC_RATE_SYNC_QUERY : FETCH_QUESTIONS_QUERY);
        requestBody.put("operationName", "problemsetQuestionListV2");

        // We use simple Maps here instead of the complex QuestionSearchRequest object
        // This allows us to delete QuestionSearchRequest.java and related Enums.
        Map<String, Object> variables = new HashMap<>();
        variables.put("skip", 0);
        variables.put("limit", 10000);
        variables.put("categorySlug", "all-code-essentials");
        variables.put("searchKeyword", "");

        // Hardcoded "Custom Ascending" sort
        variables.put("sortBy", Map.of("sortField", "CUSTOM", "sortOrder", "ASCENDING"));

        // Hardcoded "Empty" filters
        Map<String, Object> filters = new HashMap<>();
        filters.put("filterCombineType", "ALL");
        filters.put("difficultyFilter", Map.of("difficulties", new ArrayList<>(), "operator", "IS"));
        filters.put("languageFilter", Map.of("languageSlugs", new ArrayList<>(), "operator", "IS"));
        filters.put("topicFilter", Map.of("topicSlugs", new ArrayList<>(), "operator", "IS"));

        variables.put("filters", filters);
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForObject(leetcodeApiUrl, entity, QuestionListResponse.class);
    }

    /**
     * Fetches the most recent Accepted submissions for a user.
     * CRITICAL for the Spaced Repetition System.
     */
    @RateLimiter(name = "leetcode-api")
    public UserRecentSubmissionsResponse fetchUserRecentSubmissions(String username, int limit) {
        HttpHeaders headers = new HttpHeaders();
        setHeader(headers);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", FETCH_USER_RECENT_SUBMISSIONS);

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("limit", limit);
        requestBody.put("variables", variables);
        requestBody.put("operationName", "recentAcSubmissions");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForObject(leetcodeApiUrl, entity, UserRecentSubmissionsResponse.class);
    }

    /**
     * Sets the necessary HTTP headers for the request.
     */
    public void setHeader(HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        headers.add("Referer", "https://leetcode.com");
        headers.add("Origin", "https://leetcode.com");
    }
}