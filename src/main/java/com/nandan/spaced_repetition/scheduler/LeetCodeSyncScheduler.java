package com.nandan.spaced_repetition.scheduler;

import com.nandan.spaced_repetition.client.LeetCodeClient;
import com.nandan.spaced_repetition.dto.QuestionListResponse;
import com.nandan.spaced_repetition.entity.QuestionEntity;
import com.nandan.spaced_repetition.mappers.QuestionMapper;
import com.nandan.spaced_repetition.repository.QuestionsRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import java.text.DateFormat;
import java.util.Objects;

import static com.nandan.spaced_repetition.constants.Constants.*;

@Component
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class LeetCodeSyncScheduler {

    private final LeetCodeClient leetCodeApiClient;
    private final QuestionsRepository questionRepository;
    // We keep the mapper to convert the API response to our simplified Entity
    private final QuestionMapper questionMapper;

    public LeetCodeSyncScheduler(LeetCodeClient leetCodeApiClient,
                                 QuestionsRepository questionRepository,
                                 QuestionMapper questionMapper) {
        this.leetCodeApiClient = leetCodeApiClient;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    // Runs every week for full data sync
    @Async
    @Scheduled(fixedRate = WEEK_IN_MILLISECONDS)
    public void syncQuestionData() {
        Logger.info("Starting LeetCode [Full Data] sync... at {}", DateFormat.getDateInstance().format(System.currentTimeMillis()));
        long startTime = System.currentTimeMillis();

        // Fetch all questions
        QuestionListResponse response = leetCodeApiClient.fetchAllQuestions(false);

        for (QuestionListResponse.Question dto : response.getData().getProblemsetQuestionListV2().getQuestions()) {

            QuestionEntity existingQuestion = questionRepository.findByTitleSlug(dto.getTitleSlug());
            if(Objects.isNull(existingQuestion)){
                existingQuestion = new QuestionEntity();
            }

            // Update fields (Tags are now ignored)
            existingQuestion.setId(dto.getId());
            existingQuestion.setTitle(dto.getTitle());
            existingQuestion.setTitleSlug(dto.getTitleSlug());
            existingQuestion.setDifficulty(dto.getDifficulty());
            existingQuestion.setIsPaidOnly(dto.getPaidOnly());
            existingQuestion.setAcRate(dto.getAcRate());
            existingQuestion.setProblemUrl(dto.getProblemUrl());

            questionRepository.save(existingQuestion);
        }
        long endTime = System.currentTimeMillis();
        Logger.info("LeetCode [Full Data] sync completed in {} seconds.", (endTime - startTime) / 1000);
    }

    @Scheduled(fixedRate = DAY_IN_MILLISECONDS) // Runs every day for AC Rate sync
    @Async
    public void syncAcRateData() {
        Logger.info("Starting LeetCode [AC Rate] sync... at {}", DateFormat.getDateInstance().format(System.currentTimeMillis()));
        Long startTime = System.currentTimeMillis();
        QuestionListResponse response = leetCodeApiClient.fetchAllQuestions(true);

        for (QuestionListResponse.Question dto : response.getData().getProblemsetQuestionListV2().getQuestions()) {
            QuestionEntity existingQuestion = questionRepository.findByTitleSlug(dto.getTitleSlug());
            if(Objects.nonNull(existingQuestion)){
                existingQuestion.setAcRate(dto.getAcRate());
                questionRepository.save(existingQuestion);
            }
        }
        Long endTime = System.currentTimeMillis();
        Logger.info("LeetCode [AC Rate] sync completed in {} seconds.", (endTime - startTime) / 1000);
    }
}