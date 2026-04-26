package org.example.mycute.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDTO {
    private Integer userId;
    private Integer rateScore;
    private String feedbackComments;
    private LocalDateTime feedbackTime;
}
