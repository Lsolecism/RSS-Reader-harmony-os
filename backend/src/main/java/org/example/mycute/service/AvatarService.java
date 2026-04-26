package org.example.mycute.service;

import org.example.mycute.domain.entity.Avatar;

public interface AvatarService {
    Avatar getAvatarByUserId(Integer userId);
}
