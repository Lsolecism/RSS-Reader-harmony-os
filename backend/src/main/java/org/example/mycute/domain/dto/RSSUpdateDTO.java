package org.example.mycute.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSSUpdateDTO {
    private Integer userId;
    private List<Integer> deleteRSSItemIds;
    private List<Integer> deleteRSSIds;
    private List<Integer> likeRSSItemIds;
    private List<Integer> readRSSItemIds;
}
