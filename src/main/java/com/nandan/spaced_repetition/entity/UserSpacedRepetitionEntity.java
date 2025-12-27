package com.nandan.spaced_repetition.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_spaced_repetition")
public class UserSpacedRepetitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String titleSlug;

    // SM-2 Algorithm fields
    private Double easeFactor = 2.5; // Standard default
    private Integer intervalDays = 0;
    private Integer repetitions = 0;

    private LocalDateTime lastSolvedDate;
    private LocalDateTime nextReviewDate;

    public UserSpacedRepetitionEntity(String username, String titleSlug) {
        this.username = username;
        this.titleSlug = titleSlug;
        this.lastSolvedDate = LocalDateTime.now();
        this.nextReviewDate = LocalDateTime.now().minusMinutes(1); // Default review tomorrow
    }
}