package org.example.mycute.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.RSSDTO;
import org.example.mycute.domain.dto.RSSUpdateDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.RSSItem;
import org.example.mycute.domain.entity.RSSSubscription;
import org.example.mycute.domain.entity.RSSSubscriptionWithItems;
import org.example.mycute.mapper.RSSItemMapper;
import org.example.mycute.mapper.RSSSubscriptionMapper;
import org.example.mycute.mapper.UserMapper;
import org.example.mycute.service.RSSService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class RSSServiceImpl implements RSSService {
    private final UserMapper userMapper;
    private final RSSSubscriptionMapper rssSubscriptionMapper;
    private final RSSItemMapper rssItemMapper;
    private final RSSFeedProcessorServiceImpl rssFeedProcessorServiceImpl;
    @Resource(name = "rssTaskExecutor")
    private Executor taskExecutor;

//    那么我得先处理like、read的逻辑
    @Override
    @Transactional
    public ResultDTO<?> updateRSS(@NotNull RSSUpdateDTO rssUpdateDTO) {
        log.info("更新RSS{}",rssUpdateDTO);
        List<Integer> deleteRSSItemIds = Optional.ofNullable(rssUpdateDTO.getDeleteRSSItemIds()).orElse(Collections.emptyList());
        List<Integer> deleteRSSIds = Optional.ofNullable(rssUpdateDTO.getDeleteRSSIds()).orElse(Collections.emptyList());
        List<Integer> likeRSSItemIds = Optional.ofNullable(rssUpdateDTO.getLikeRSSItemIds()).orElse(Collections.emptyList());
        List<Integer> readRSSItemIds = Optional.ofNullable(rssUpdateDTO.getReadRSSItemIds()).orElse(Collections.emptyList());
        try {
            if (!readRSSItemIds.isEmpty()) {
                int batchSize = 100;
                for (int i = 0; i < readRSSItemIds.size(); i += batchSize) {
                    List<Integer> batchIds = readRSSItemIds.subList(i, Math.min(i + batchSize, readRSSItemIds.size()));
                    UpdateWrapper<RSSItem> updateWrapper = new UpdateWrapper<>();
                    updateWrapper
                            .setSql("readed = 1 - COALESCE(readed, 0)") // 切换状态
                            .in("rss_item_id", batchIds);
                    rssItemMapper.update(null, updateWrapper);
                }
            }
        } catch (Exception e) {
            log.error("阅读状态切换更新失败", e);
        }

// 点赞状态切换更新
        try {
            if (!likeRSSItemIds.isEmpty()) {
                int batchSize = 1000;
                for (int i = 0; i < likeRSSItemIds.size(); i += batchSize) {
                    List<Integer> batchIds = likeRSSItemIds.subList(i, Math.min(i + batchSize, likeRSSItemIds.size()));
                    UpdateWrapper<RSSItem> updateWrapper = new UpdateWrapper<>();
                    updateWrapper
                            .setSql("liked = 1 - COALESCE(liked, 0)") // 切换状态
                            .in("rss_item_id", batchIds);
                    rssItemMapper.update(null, updateWrapper);
                }
            }
        } catch (Exception e) {
            log.error("喜欢状态切换更新失败", e);
        }
        try{
            if (!deleteRSSItemIds.isEmpty()){
                for (Integer rssItemId : deleteRSSItemIds){
                    rssItemMapper.deleteById(rssItemId);
                    rssSubscriptionMapper.deleteRssItem(rssItemId);
                }
            }
        }catch (Exception e){
            log.error("删除孩子更新失败", e);
            return new ResultDTO<>(null, 500, "删除Item失败");
        }
        try{
            if (!deleteRSSIds.isEmpty()){
                for (Integer rssId : deleteRSSIds){
                    rssSubscriptionMapper.deleteRssSubscription(rssId);
                    rssSubscriptionMapper.deleteById(rssId);
                    userMapper.deleteRssSubscription(rssId);
                }
            }
        }catch (Exception e){
            log.error("删除爸爸更新失败", e);
            return new ResultDTO<>(null, 500, "删除RSS订阅失败");
        }
        return new ResultDTO<>(null, 200, "更新成功");
    }


    @Override
    public ResultDTO<List<RSSSubscriptionWithItems>> initRSS(@NotNull RSSDTO rssDTO) {
        Integer userId = rssDTO.getUserId();
        List<RSSSubscription> subscriptions = rssSubscriptionMapper.findRSSSubccriptionByUserId(userId);

        // 结果收集容器（线程安全）
        List<RSSSubscriptionWithItems> result = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (RSSSubscription subscription : subscriptions) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    RSSSubscriptionWithItems updated = rssFeedProcessorServiceImpl.processSingleFeed(subscription);
                    if (updated != null) {
                        result.add(updated);
                    }
                } catch (Exception e) {
                    log.error("处理RSS源失败: {}", subscription.getRssFeed(), e);
                }
            }, taskExecutor));
        }

        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("RSS{}",result);
        return new ResultDTO<>(result, 200, "RSS更新完成");
    }
/*
    每次登录查询rss订阅是否有更新，如果有的话拉取并且插入数据库，并且返回给前端最新的数据
    @Override
    @Transactional
    public ResultDTO<List<RSSSubscriptionWithItems>> initRSS(@NotNull RSSDTO rssDTO) {
        Integer userId = rssDTO.getUserId();
        List<RSSSubscription> rssSubscriptions = rssSubscriptionMapper.findRSSSubccriptionByUserId(userId);
        List<RSSSubscriptionWithItems> listRssSubscriptionWithItems = new ArrayList<>();
        for (RSSSubscription rssSubscription : rssSubscriptions) {
            String rssFeed = rssSubscription.getRssFeed();
            try {
                URL feedUrl = new URL(rssFeed);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedUrl));
                List<SyndEntry> entries = feed.getEntries();
                Date newTime = rssSubscription.getNewTime();
                for (SyndEntry entry : entries) {
                    if (entry.getPublishedDate().after(rssSubscription.getNewTime())){
                        RSSItem rssItem = new RSSItem(null,entry.getTitle(), entry.getAuthor(), entry.getUri(),
                                entry.getLink(), entry.getDescription().getValue(),  entry.getContents().get(0).getValue(),
                                entry.getPublishedDate(),null,null);
                        rssItemMapper.insert(rssItem);
                        rssSubscriptionMapper.insertRssRssSubscription(rssSubscription.getRssId(), rssItem.getRssItemId());
                        newTime  = entry.getPublishedDate();
                    }else{
                        rssSubscription.setNewTime(newTime);
                        rssSubscriptionMapper.updateById(rssSubscription);
                        break;
                    }
                }
                listRssSubscriptionWithItems.add(new RSSSubscriptionWithItems(rssSubscription,rssItemMapper.findRSSItemByRssId(rssSubscription.getRssId())));
            } catch (IOException | FeedException e) {
                log.error("获取RSS失败", e);
                throw new RuntimeException(e);
            }
        }
        return new ResultDTO<>(listRssSubscriptionWithItems, 200, "获取RSS成功");
    }
*/


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<RSSSubscriptionWithItems> getRSSItems(@NotNull RSSDTO rssDTO) {
        Integer userId = rssDTO.getUserId();
        String rssTitle = rssDTO.getRssTitle();
        String rssFeed = rssDTO.getRssFeed();
        RSSSubscriptionWithItems rssSubscriptionWithItems = new RSSSubscriptionWithItems();
        List<RSSItem> rssItems = new ArrayList<>();
        log.info("rssDTO:{}",rssDTO);
        try {
            URL feedUrl = new URL(rssFeed);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            log.info("feed:{}",feed.getPublishedDate());
            Date publishedDate = feed.getPublishedDate();
            log.info("publishedDate:{}",publishedDate);
            List<SyndEntry> entries = feed.getEntries();
            log.info("entries:{}",entries);
            RSSSubscription rssSubscription = new RSSSubscription(null, rssTitle, rssFeed,publishedDate);
            rssSubscriptionMapper.insert(rssSubscription);
            for (SyndEntry entry : entries) {
                RSSItem rssItem = new RSSItem(null,entry.getTitle(), entry.getAuthor(), entry.getUri(),
                        entry.getLink(), entry.getDescription().getValue(), entry.getContents().get(0).getValue(),
                        entry.getPublishedDate(),null,null);
                log.info("获取看看这次的这个contents:{}",rssItem);
                rssItemMapper.insert(rssItem);
                rssSubscriptionMapper.insertRssRssSubscription(rssSubscription.getRssId(), rssItem.getRssItemId());
                rssItems.add(rssItem);
            }
            int insert =  userMapper.insertUserRss(userId, rssSubscription.getRssId());
            if (insert == 0){
                log.info("插入失败");
            }
            rssSubscriptionWithItems.setRssSubscription(rssSubscription);
            rssSubscriptionWithItems.setRssItems(rssItems);
        } catch (IOException | FeedException e) {
            log.error("获取RSS失败", e);
            return new ResultDTO<>(null, 500, "获取RSS失败");
        }
        return new ResultDTO<>(rssSubscriptionWithItems, 200, "获取RSS成功");
    }
}
