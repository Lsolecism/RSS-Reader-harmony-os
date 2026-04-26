package org.example.mycute.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.Music;
import org.example.mycute.service.MusicService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/get-music")
    public ResultDTO<List<Music>> getMusic() {
        return musicService.getMusic();
    }
}
