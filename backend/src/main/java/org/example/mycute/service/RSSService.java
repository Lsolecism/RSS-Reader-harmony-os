package org.example.mycute.service;



import org.example.mycute.domain.dto.RSSDTO;
import org.example.mycute.domain.dto.RSSUpdateDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.RSSSubscriptionWithItems;

import java.util.List;

public interface RSSService {
    ResultDTO<RSSSubscriptionWithItems> getRSSItems(RSSDTO rssDTO);
    ResultDTO<List<RSSSubscriptionWithItems>> initRSS(RSSDTO rssDTO);
    ResultDTO<?> updateRSS(RSSUpdateDTO rssUpdateDTO);
}
