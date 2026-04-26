package org.example.mycute.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.entity.Avatar;
import org.example.mycute.mapper.AvatarMapper;
import org.example.mycute.service.AvatarService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {
    private final AvatarMapper avatarMapper;
//  得到头像
    @Override
    public Avatar getAvatarByUserId(Integer userId) {
        log.info("getAvatarByUserId");
        return avatarMapper.findUserAvatarByUserId(userId);
    }
}
