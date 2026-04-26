package org.example.mycute.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("feedback")
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {
    @TableId(value = "feedback_id", type = IdType.AUTO) // 主键映射
    private Integer feedbackId;
    @TableField(value = "feedback_comments")
    private String feedbackComments;
    @TableField(value = "feedback_time")
    private LocalDateTime feedbackTime;
    @TableField(value = "rate_score")
    private Integer rateScore;
}
