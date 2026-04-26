package org.example.mycute.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.mycute.domain.entity.RSSSubscription;

import java.util.List;

@Mapper
public interface RSSSubscriptionMapper extends BaseMapper<RSSSubscription> {
    @Select("SELECT a.* FROM rss_subscription a " +
            "INNER JOIN user_rss_subscription ua ON a.rss_id = ua.rss_id " +
            "WHERE ua.user_id = #{userId}")
    List<RSSSubscription> findRSSSubccriptionByUserId(Integer userId);

    @Insert("INSERT INTO  user.rss_subscription_rss_item(rss_id,rss_item_id) VALUES (#{rssId}, #{rssItemId})")
    int insertRssRssSubscription(@Param("rssId") Integer rssId, @Param("rssItemId") Integer rssItemId);

    @Delete("DELETE FROM user.rss_subscription_rss_item WHERE rss_item_id = #{rssItemId}")
    int deleteRssItem(@Param("rssItemId") Integer rssItemId);

    @Delete("DELETE FROM user.rss_subscription_rss_item WHERE rss_id = #{rssId}")
    int deleteRssSubscription(@Param("rssId") Integer rssId);

}
