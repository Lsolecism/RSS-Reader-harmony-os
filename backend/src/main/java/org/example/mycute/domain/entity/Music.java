package org.example.mycute.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("music")
@AllArgsConstructor
@NoArgsConstructor
public class Music {
    @TableField(value = "id")
    private String  id;
    @TableField(value = "name")
    private String  name;
    @TableField(value = "author")
    private String  author;
    @TableField(value = "img")
    private String  img;
    @TableField(value = "url")
    private String  url;
}
