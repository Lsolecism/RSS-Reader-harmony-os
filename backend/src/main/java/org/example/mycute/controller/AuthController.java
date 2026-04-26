package org.example.mycute.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.*;
import org.example.mycute.domain.entity.User;
import org.example.mycute.mapper.UserMapper;
import org.example.mycute.service.AuthService;
import org.example.mycute.service.MQProducerService;
import org.example.mycute.utils.JwtTokenProvider;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final MQProducerService mqProducerService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResultDTO<AuthResponseDTO> login(@Valid @RequestBody LoginFormDTO loginForm) {
        return authService.login(loginForm);
    }

    @PostMapping("/register")
    public ResultDTO<AuthResponseDTO> register(@Valid @RequestBody RegisterFormDTO registerForm) {
        return authService.register(registerForm);
    }

    @PostMapping("/send-code")
    public ResultDTO<?> sendCode(@Valid @RequestBody LoginFormDTO getVerificationDTO) {
        log.info("邮箱：{}", getVerificationDTO);
        String email = getVerificationDTO.getEmail();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            return new ResultDTO<>(null, 500, "邮箱不存在");
        }
        String code = authService.generateAndStoreCode(email);
        mqProducerService.sendEmailTask(email, code);
        return new ResultDTO<>(null, 200, "验证码发送成功");
    }

    @PostMapping("/verify-code")
    public ResultDTO<AuthResponseDTO> verifyCode(@Valid @RequestBody LoginFormDTO verificationDTO) {
        log.info("邮箱：{}", verificationDTO);
        if (authService.verifyCode(verificationDTO.getEmail(), verificationDTO.getVerificationCode())) {
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", verificationDTO.getEmail()));
            // 生成JWT token
            String token = jwtTokenProvider.generateToken(user.getUserId());
            log.info("token是{}", token);
            // 创建响应DTO
            AuthResponseDTO response = new AuthResponseDTO(user, token);
            return new ResultDTO<>(response, 200, "登录成功");
        } else {
            return new ResultDTO<>(null, 500, "验证失败");
        }
    }
}
