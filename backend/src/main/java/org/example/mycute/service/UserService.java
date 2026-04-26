package org.example.mycute.service;

import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.dto.UserDTO;
import org.example.mycute.domain.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

    ResultDTO<UserInfoDTO> updateUserInfo(UserInfoDTO userInfoDTO);
    ResultDTO<?> updateUserAvatarByHMOS(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") Integer userId);
    ResultDTO<?> updateUserInfoByHMOS(UserDTO userDTO);
}
