package com.nandan.spaced_repetition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nandan.spaced_repetition.client.LeetCodeClient;
import com.nandan.spaced_repetition.dto.UserRecentSubmissionsResponse;
import com.nandan.spaced_repetition.exceptions.BadRequestException;
import com.nandan.spaced_repetition.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeetCodeService {

    private final LeetCodeClient leetCodeClient;
    private final ObjectMapper objectMapper;

    public Optional<UserRecentSubmissionsResponse.DataNode> getUserRecentSubmissions(String username, int limit) {
        if(limit <= 0){
            throw new BadRequestException("Limit must be greater than 0");
        }

        Logger.info("Fetching recent submissions for user: {}", username);
        var response = leetCodeClient.fetchUserRecentSubmissions(username, limit);

        if (Objects.nonNull(response) && Objects.nonNull(response.getData())) {
            Logger.info("Recent submissions found for user: {}", username);
            var dataNode = response.getData();
            Logger.debug("Recent submissions details: {}", dataNode);
            return Optional.of(dataNode);
        }

        Logger.warn("No recent submissions found for user: {}", username);
        throw new UserNotFoundException(username);
    }
}