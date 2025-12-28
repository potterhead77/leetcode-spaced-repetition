package com.nandan.spaced_repetition.repository;

import com.nandan.spaced_repetition.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends JpaRepository<QuestionEntity, Integer> {

    // This is the only custom method you need now
    QuestionEntity findByTitleSlug(String titleSlug);

}