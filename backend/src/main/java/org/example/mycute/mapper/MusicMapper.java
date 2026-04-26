package org.example.mycute.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.mycute.domain.entity.Music;

import java.util.List;

@Mapper
public interface MusicMapper extends BaseMapper<Music> {
    @Select("SELECT * FROM music ORDER BY RAND() LIMIT 8")
    List<Music> selectRandomMusics();
}
