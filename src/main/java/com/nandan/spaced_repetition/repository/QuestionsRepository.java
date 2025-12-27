package com.nandan.spaced_repetition.repository;

import com.nandan.spaced_repetition.entity.QuestionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionsRepository extends JpaRepository<QuestionEntity,Integer>, JpaSpecificationExecutor<QuestionEntity> {

    QuestionEntity findByTitleSlug(String title);
    QuestionEntity findByIsProblemOfTheDayTrue();

    @Query("SELECT t.name as tagName, COUNT(q) as totalCount " +
            "FROM QuestionEntity q JOIN q.topicTags t " +
            "GROUP BY t.name")
    List<TagCountProjection> countProblemsByTag();
    @Query("SELECT DISTINCT q FROM QuestionEntity q JOIN q.topicTags t " +
            "WHERE t.name IN :targetTags AND q.difficulty = 'MEDIUM' " +
            "ORDER BY q.acRate DESC LIMIT 50")
    List<QuestionEntity> findCandidateQuestions(List<String> targetTags);

    @Query(value = "SELECT DISTINCT q.* FROM leetcode_questions q " +
            "JOIN question_topic_tags t ON q.id = t.question_id " +
            "WHERE t.name IN :tags AND q.difficulty IN :difficulties " +
            "ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<QuestionEntity> findRandomQuestionsByTagsAndDifficulty(List<String> tags, List<String> difficulties, int limit);
    @Transactional
    void deleteByIsProblemOfTheDayTrue();
}
