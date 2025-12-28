package com.nandan.spaced_repetition.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leetcode_questions")
public class QuestionEntity {
    @Id
    private int id; // LeetCode's frontend ID

    @Column(unique = true)
    private String titleSlug; // "two-sum"

    private String title;      // "Two Sum"
    private String difficulty; // "Easy"
    private Double acRate;     // 45.5
    private Boolean isPaidOnly;
    private String problemUrl;
}