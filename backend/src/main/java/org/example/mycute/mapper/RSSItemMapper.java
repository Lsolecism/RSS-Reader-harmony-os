package org.example.mycute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.mycute.domain.entity.RSSItem;

import java.util.List;

@Mapper
public interface RSSItemMapper extends BaseMapper<RSSItem> {
    @Select("SELECT a.* FROM rss_item a " +
            "INNER JOIN rss_subscription_rss_item ua ON a.rss_item_id = ua.rss_item_id " +
            "WHERE ua.rss_id = #{rssId}")
    List<RSSItem> findRSSItemByRssId(Integer rssId);

}
