package org.example.mycute.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.dto.UserDTO;
import org.example.mycute.domain.dto.UserInfoDTO;
import org.example.mycute.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/update")
    public ResultDTO<UserInfoDTO> updateUserInfo(@Valid @RequestBody UserInfoDTO userInfoDTO) {
        log.info("updateUserInfo: {}", userInfoDTO);
        return userService.updateUserInfo(userInfoDTO);
    }

    @PostMapping("/updateAvatarByHMOS")
    ResultDTO<?> updateUserAvatarByHMOS(
             MultipartFile file){
        log.info("minzi{}",file.getOriginalFilename() );
        Integer userId = Integer.parseInt(Objects.requireNonNull(file.getOriginalFilename()));
        return userService.updateUserAvatarByHMOS(file,userId);
    }

    @PostMapping("/updateInfoByHarmonyOS")
    ResultDTO<?> updateUserInfoByHarmonyOS(
            @RequestBody @Valid UserDTO userDTO
    ){
        return userService.updateUserInfoByHMOS(userDTO);
    }
}
