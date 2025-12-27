package com.nandan.spaced_repetition.service;

import com.nandan.spaced_repetition.dto.QuestionResponseDTO;
import com.nandan.spaced_repetition.dto.QuestionSearchRequest;
import com.nandan.spaced_repetition.entity.QuestionEntity;
import com.nandan.spaced_repetition.enums.questions.SortOrder;
import com.nandan.spaced_repetition.mappers.QuestionMapper;
import com.nandan.spaced_repetition.repository.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeetCodeQuestionsService {

    private final QuestionsRepository questionRepository;
    private final QuestionSpecificationService specificationService;
    private final QuestionMapper questionMapper;

    public Page<QuestionResponseDTO> findQuestions(QuestionSearchRequest request) {

        Pageable pageable = createPageable(request);

        Specification<QuestionEntity> spec = specificationService.build(
                request.getFilters(), request.getSearchKeyword()
        );

        Page<QuestionEntity> entityPage = questionRepository.findAll(spec, pageable);

        return entityPage.map(questionMapper::entityToResponseDTO);
    }

    /**
     * Helper method to convert your DTOs into a Spring Data Pageable object.
     */
    private Pageable createPageable(QuestionSearchRequest request) {

        int size = (request.getLimit() <= 0) ? 10 : request.getLimit();
        int page = request.getSkip() / size;

        Sort sort = Sort.unsorted(); // Default


        if (request.getSortBy() != null && request.getSortBy().getSortField() != null) {

            QuestionSearchRequest.SortingCriteria sortBy = request.getSortBy();

            Sort.Direction direction = (sortBy.getSortOrder() == SortOrder.DESCENDING) ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            String field = switch (sortBy.getSortField()) {
                case AC_RATE -> "acRate";
                case FRONTEND_ID, CUSTOM -> "id";
                case DIFFICULTY -> "difficulty";
            };

            sort = Sort.by(direction, field);
        }

        return PageRequest.of(page, size, sort);
    }
}
