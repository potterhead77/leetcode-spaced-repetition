package com.nandan.spaced_repetition.controller;

import com.nandan.spaced_repetition.dto.QuestionResponseDTO;
import com.nandan.spaced_repetition.entity.QuestionEntity;
import com.nandan.spaced_repetition.mappers.QuestionMapper;
import com.nandan.spaced_repetition.repository.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class LeetCodeQuestionsController {

    // Removed LeetCodeQuestionsService dependency as it was only for search
    private final QuestionsRepository questionsRepository;
    private final QuestionMapper questionMapper;

    /**
     * Standard paginated endpoint to get questions from YOUR database.
     * Useful for verifying your Weekly Sync is working.
     * Usage: GET /api/v1/questions?page=0&size=20
     */
    @GetMapping()
    public ResponseEntity<Page<QuestionResponseDTO>> getAllQuestions(@ParameterObject Pageable pageable){
        Logger.info("Fetching questions from database with pagination: {}", pageable);

        Page<QuestionEntity> entityPage = questionsRepository.findAll(pageable);
        Page<QuestionResponseDTO> dtoPage = entityPage.map(questionMapper::entityToResponseDTO);

        return ResponseEntity.ok(dtoPage);
    }

    // DELETED: searchQuestions() - You don't need a search engine for a reminder app.
    // DELETED: getPotd() - You aren't tracking the global daily challenge.
}