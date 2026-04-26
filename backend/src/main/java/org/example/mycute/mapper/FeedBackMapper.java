package org.example.mycute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.mycute.domain.entity.FeedBack;

import java.util.List;

@Mapper
public interface FeedBackMapper extends BaseMapper<FeedBack> {

    @Select(
            "SELECT a.* FROM feedback a " +
                    "INNER JOIN user_feedback ua ON a.feedback_id = ua.feedback_id " +
                    "WHERE ua.user_id = #{userId} " +
                    "LIMIT #{pageSize} OFFSET #{offset}"  // 添加分页参数
    )
    List<FeedBack> findFeedBackByUserIdPage(
            @Param("userId") Integer userId,
            @Param("offset") Integer offset,         // 偏移量 = (pageNum-1)*pageSize
            @Param("pageSize") Integer pageSize
    );

    @Select("SELECT COUNT(*) FROM feedback a " +
            "INNER JOIN user_feedback ua ON a.feedback_id = ua.feedback_id " +
            "WHERE ua.user_id = #{userId}")
    Integer selectTotalUserCount(@Param("userId") Integer userId);

    @Select("SELECT a.* FROM feedback a LIMIT #{pageSize} OFFSET #{offset}")
    List<FeedBack> findAllFeedbackPage(
            @Param("offset") Integer offset,         // 偏移量 = (pageNum-1)*pageSize
            @Param("pageSize") Integer pageSize
    );

    @Select("SELECT COUNT(*) FROM feedback")
    Integer selectTotalCount();

}
