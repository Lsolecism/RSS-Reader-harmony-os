package org.example.mycute.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rss_item")
public class RSSItem {
    @TableId(value = "rss_item_id", type = IdType.AUTO)
    private Integer rssItemId;
    @TableField(value = "title")
    private String title;
    @TableField(value = "author")
    private String author;
    @TableField(value = "uri")
    private String uri;
    @TableField(value = "link")
    private String link;
    @TableField(value = "description")
    private String description;
    @TableField(value = "contents")
    private String contents;
    @TableField(value = "pub_date")
    private Date pubDate;
    @TableField(value = "readed")
    private Integer readed;
    @TableField(value = "liked")
    private Integer liked;
}
