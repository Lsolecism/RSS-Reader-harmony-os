package org.example.mycute.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@TableName("rss_subscription")
@AllArgsConstructor
@NoArgsConstructor
public class RSSSubscription {
    @TableId(value = "rss_id", type = IdType.AUTO)
    private Integer rssId;
    @TableField(value = "rss_title")
    private String rssTitle;
    @TableField(value = "rss_feed")
    private String rssFeed;
    @TableField(value = "new_time")
    private Date newTime;
}
