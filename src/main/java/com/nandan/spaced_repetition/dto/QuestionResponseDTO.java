// IN: com.rajat_singh.leetcode_api.dto.QuestionResponseDTO.java
package com.nandan.spaced_repetition.dto;

import com.nandan.spaced_repetition.entity.TopicTag; // Use your @Embeddable
import lombok.Data;
import java.util.List;

@Data
public class QuestionResponseDTO {
    // This DTO represents a SINGLE question from our database
    private int id;
    private String difficulty;
    private String title;
    private String titleSlug;
    private Double acRate;
    private Boolean isPaidOnly;
    private String problemUrl;
    private List<TopicTag> topicTags;
}