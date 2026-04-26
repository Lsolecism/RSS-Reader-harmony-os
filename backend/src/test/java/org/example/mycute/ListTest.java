package org.example.mycute;

import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.RSSDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.RSSItem;
import org.example.mycute.domain.entity.RSSSubscriptionWithItems;
import org.example.mycute.mapper.RSSItemMapper;
import org.example.mycute.service.RSSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class ListTest {
    @Autowired
    private RSSItemMapper rSSItemMapper;

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
    }

    @Test
    public void testInitRSS() {
        // 1. 准备测试参数（使用实际存在的用户ID）
        Integer testRssItemId = 1; // 替换为实际存在的用户ID
        RSSItem rssItem = rSSItemMapper.selectById(testRssItemId);
        log.info("RSSItem: {}", rssItem);

    }
}
