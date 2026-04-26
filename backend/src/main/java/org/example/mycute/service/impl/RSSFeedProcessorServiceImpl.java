package org.example.mycute.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.entity.RSSItem;
import org.example.mycute.domain.entity.RSSSubscription;
import org.example.mycute.domain.entity.RSSSubscriptionWithItems;
import org.example.mycute.mapper.RSSItemMapper;
import org.example.mycute.mapper.RSSSubscriptionMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RSSFeedProcessorServiceImpl {
    
    private final RSSItemMapper rssItemMapper;
    private final RSSSubscriptionMapper rssSubscriptionMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RSSSubscriptionWithItems processSingleFeed(@NotNull RSSSubscription subscription) {
        String rssFeed = subscription.getRssFeed();
        try {
            URL feedUrl = new URL(rssFeed);
            SyndFeedInput input = new SyndFeedInput();
            // 在build方法前设置超时
            URLConnection conn = feedUrl.openConnection();
            conn.setConnectTimeout(5000); // 5秒连接超时
            conn.setReadTimeout(10000);   // 10秒读取超时
            SyndFeed feed = input.build(new XmlReader(conn));

            List<SyndEntry> entries = feed.getEntries();
            if (entries.isEmpty()) {
                return new RSSSubscriptionWithItems(subscription, 
                    rssItemMapper.findRSSItemByRssId(subscription.getRssId()));
            }

            // 按发布时间排序（从新到旧）
            entries.sort((e1, e2) -> e2.getPublishedDate().compareTo(e1.getPublishedDate()));
            
            Date latestTime = entries.get(0).getPublishedDate(); // 获取最新的时间
            Date currentNewTime = subscription.getNewTime();
            List<RSSItem> newItems = new ArrayList<>();

            // 最多处理20条新内容
            int processedCount = 0;
            final int MAX_ITEMS = 20;

            for (SyndEntry entry : entries) {
                // 如果已经处理了足够多的条目或遇到旧条目，就停止
                if (processedCount >= MAX_ITEMS || !entry.getPublishedDate().after(currentNewTime)) {
                    break;
                }

                RSSItem item = new RSSItem(null, entry.getTitle(), entry.getAuthor(),
                        entry.getUri(), entry.getLink(),
                        entry.getDescription().getValue(),
                        entry.getContents().get(0).getValue(),
                        entry.getPublishedDate(), null, null);
                rssItemMapper.insert(item);
                rssSubscriptionMapper.insertRssRssSubscription(subscription.getRssId(), item.getRssItemId());
                newItems.add(item);
                processedCount++;
            }

            // 更新订阅源时间为最新条目的时间
            if (!newItems.isEmpty()) {
                subscription.setNewTime(latestTime);
                rssSubscriptionMapper.updateById(subscription);
            }

            // 获取该订阅源的所有文章
            List<RSSItem> allItems = rssItemMapper.findRSSItemByRssId(subscription.getRssId());
            return new RSSSubscriptionWithItems(subscription, allItems);
        } catch (Exception e) {
            log.error("处理RSS源失败: {}", rssFeed, e);
            // 即使处理失败，也返回现有的文章
            return new RSSSubscriptionWithItems(subscription, 
                rssItemMapper.findRSSItemByRssId(subscription.getRssId()));
        }
    }
} 