package org.example.mycute.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.Music;
import org.example.mycute.mapper.MusicMapper;
import org.example.mycute.service.MusicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {
    private final MusicMapper musicMapper;
    @Override
    public ResultDTO<List<Music>> getMusic() {
        try{
            List<Music> musicList = musicMapper.selectRandomMusics();
            return new ResultDTO<>(musicList, 200, "获取成功");
        }catch (Exception e)
            {
            return new ResultDTO<>(null, 500, e.toString());
        }
    }
}
