package org.example.mycute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.*;
import org.example.mycute.domain.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Insert("INSERT INTO user.user_avatar (user_id, avatar_id) VALUES (#{userId}, #{avatarId})")
    int insertUserAvatar(@Param("userId") Integer userId, @Param("avatarId") Integer avatarId);

    @Insert("INSERT INTO user.user_rss_subscription (user_id, rss_id) VALUES (#{userId}, #{rssId})")
    int insertUserRss(@Param("userId") Integer userId, @Param("rssId") Integer rssId);

    @Insert("INSERT INTO  user.user_feedback(user_id,feedback_id) VALUES (#{userId}, #{feedbackId})")
    int insertUserFeedback(@Param("userId") Integer userId, @Param("feedbackId") Integer feedbackId);

    @Delete("DELETE FROM user.user_rss_subscription WHERE rss_id= #{rssId}")
    void deleteRssSubscription(@Param("rssId") Integer rssId);

}