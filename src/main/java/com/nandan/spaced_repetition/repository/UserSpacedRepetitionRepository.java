package com.nandan.spaced_repetition.repository;

import com.nandan.spaced_repetition.entity.UserSpacedRepetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSpacedRepetitionRepository extends JpaRepository<UserSpacedRepetitionEntity, Long> {

    // Find a specific card for a user
    Optional<UserSpacedRepetitionEntity> findByUsernameAndTitleSlug(String username, String titleSlug);

    // Find all cards due for review (date is in the past)
    List<UserSpacedRepetitionEntity> findByUsernameAndNextReviewDateBefore(String username, LocalDateTime date);
}