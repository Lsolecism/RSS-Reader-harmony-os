package org.example.mycute.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RSSDTO {
    private Integer userId;
    private String rssTitle;
    private String rssFeed;
}
