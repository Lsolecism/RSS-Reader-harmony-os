package org.example.mycute.service;

import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.Music;

import java.util.List;

public interface MusicService {
    ResultDTO<List<Music>> getMusic();
}
