package org.example.mycute.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackGetDTO {
    private Integer userId;
    private Integer pageNum;
    private Integer pageSize;
}
