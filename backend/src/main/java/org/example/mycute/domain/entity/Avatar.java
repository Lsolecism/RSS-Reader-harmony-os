package org.example.mycute.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("avatar")
@AllArgsConstructor
@NoArgsConstructor
public class Avatar {
    @TableId(value = "avatar_id", type = IdType.AUTO) // 主键映射
    private Integer avatarId;
    private byte[] image;
    private String type;
}
