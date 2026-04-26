package org.example.mycute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.mycute.domain.entity.Avatar;

@Mapper
public interface AvatarMapper extends BaseMapper<Avatar> {
    @Select("SELECT a.* FROM avatar a " +
            "INNER JOIN user_avatar ua ON a.avatar_id = ua.avatar_id " +
            "WHERE ua.user_id = #{userId}")
    Avatar findUserAvatarByUserId(Integer userId);
}
