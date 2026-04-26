package org.example.mycute.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.RSSDTO;
import org.example.mycute.domain.dto.RSSUpdateDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.RSSSubscriptionWithItems;
import org.example.mycute.service.RSSService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/rss")
@RequiredArgsConstructor
public class RSSController {
    private final RSSService rssService;

//    添加rss源
    @PostMapping("/getRSSItems")
    public ResultDTO<RSSSubscriptionWithItems> getRSSItems(@Valid @RequestBody RSSDTO rssDTO){
        return rssService.getRSSItems(rssDTO);
    }

    @PostMapping("/initRSS")
    public ResultDTO<List<RSSSubscriptionWithItems>> initRSS(@Valid @RequestBody RSSDTO rssDTO){
        return rssService.initRSS(rssDTO);
    }
    @PostMapping("/updateRSS")
    public ResultDTO<?> updateRSS(@Valid @RequestBody RSSUpdateDTO rssUpdateDTO){
        return rssService.updateRSS(rssUpdateDTO);
    }
}
