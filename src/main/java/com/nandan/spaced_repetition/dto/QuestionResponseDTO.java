package com.nandan.spaced_repetition.dto;

import lombok.Data;

@Data
public class QuestionResponseDTO {
    // Represents a single question sent to the frontend/API user
    private int id;
    private String difficulty;
    private String title;
    private String titleSlug;
    private Double acRate;
    private Boolean isPaidOnly;
    private String problemUrl;
}