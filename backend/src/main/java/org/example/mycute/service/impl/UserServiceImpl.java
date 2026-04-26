package org.example.mycute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.dto.UserDTO;
import org.example.mycute.domain.dto.UserInfoDTO;
import org.example.mycute.domain.entity.Avatar;
import org.example.mycute.domain.entity.User;
import org.example.mycute.mapper.AvatarMapper;
import org.example.mycute.mapper.UserMapper;
import org.example.mycute.service.UserService;
import org.example.mycute.utils.ExtractImageType;
import org.example.mycute.utils.TikaUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AvatarMapper  avatarMapper;
    private final ExtractImageType extractImageType;
    private final TikaUtil tikaUtil;


    @Override
    @Transactional
    public ResultDTO<?> updateUserAvatarByHMOS(@NotNull @RequestPart("file") MultipartFile file,
                                         @RequestParam("userId") Integer userId) {
        try {
            User user = userMapper.selectById(userId);
            userMapper.updateById(user);
            Avatar avatar = avatarMapper.findUserAvatarByUserId(userId);
            avatar.setImage(file.getBytes());
            avatar.setType(tikaUtil.detectMimeType(file));
            avatarMapper.updateById(avatar);
            return new ResultDTO<>(null, 200, "更新成功");
        } catch (IOException e) {
            return new ResultDTO<>(null, 500, "更新失败");
        }
    }

    @Override
    @Transactional
    public ResultDTO<?> updateUserInfoByHMOS(@NotNull UserDTO userDTO) {

        try {
            User user = userMapper.selectById(userDTO.getUserId());
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            userMapper.updateById(user);
            return new ResultDTO<>(null, 200, "更新成功");
        }catch (Exception e)
        {
            return new ResultDTO<>(null, 500, "更新失败");
        }

    }


    @Override
    @Transactional
    public ResultDTO<UserInfoDTO> updateUserInfo(UserInfoDTO userInfoDTO) {
        try {
            log.info("updateUserInfo: {}", userInfoDTO);
            String userName = userInfoDTO.getUserName();
            String email = userInfoDTO.getEmail();
            String newPhoto = userInfoDTO.getNewPhoto();
            // 提取类型和纯数据
            String imageMIME = tikaUtil.getFileExtension(newPhoto);
            String imageType = extractImageType.extractImageType(newPhoto);
            String pureBase64 = extractImageType.extractBase64Data(newPhoto);
            // 将纯 Base64 转为字节数组
            byte[] imageBytes = Base64.getDecoder().decode(pureBase64);
            Integer userId = userInfoDTO.getUserId();
            User user = userMapper.selectById(userId);
            user.setUserName(userName);
            user.setEmail(email);
            userMapper.updateById(user);
            Avatar avatar = avatarMapper.findUserAvatarByUserId(userId);
            avatar.setImage(imageBytes);
            avatar.setType(imageType);
            avatarMapper.updateById(avatar);
            return new  ResultDTO<>(null, 200, "更新成功");
        } catch (Exception e) {
            log.error("updateUserInfo error: {}", e.getMessage());
            return new  ResultDTO<>(null, 500, "更新失败");
        }
    }
}
