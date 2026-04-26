package org.example.mycute.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.mycute.domain.dto.*;
import org.example.mycute.domain.entity.Avatar;
import org.example.mycute.domain.entity.User;
import org.example.mycute.mapper.AvatarMapper;
import org.example.mycute.mapper.UserMapper;
import org.example.mycute.service.AuthService;
import org.example.mycute.utils.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final AvatarMapper avatarMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public ResultDTO<AuthResponseDTO> login(@NotNull LoginFormDTO loginFormDTO) {
        val user = userMapper.selectOne(new QueryWrapper<User>().eq("email", loginFormDTO.getEmail()));
        if(user == null){
            return new ResultDTO<>(null, 500, "没有该用户");
        }
        if (!user.getPassWord().equals(loginFormDTO.getPassWord())) {
            return new ResultDTO<>(null, 500, "用户名或密码错误");
        }
        
        // 生成JWT token
        String token = jwtTokenProvider.generateToken(user.getUserId());
        log.info("token是{}", token);
        // 创建响应DTO
        AuthResponseDTO response = new AuthResponseDTO(user, token);
        return new ResultDTO<>(response, 200, "登录成功");
    }

    @Override
    @Transactional
    public ResultDTO<AuthResponseDTO> register(@NotNull RegisterFormDTO registerFormDTO) {
        User user = new User(null, registerFormDTO.getUserName(), registerFormDTO.getPassWord(), registerFormDTO.getEmail());
        Avatar avatar = new Avatar(null, null,null);
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("email", registerFormDTO.getEmail()));
        if(count > 0) {
            return new ResultDTO<>(null, 500, "邮箱已存在");
        }
        else{
            int userId = userMapper.insert(user);
            int avatarId = avatarMapper.insert(avatar);
            int insert = userMapper.insertUserAvatar(user.getUserId(), avatar.getAvatarId());
            if (userId == 0 || avatarId == 0 || insert == 0) {
                return new ResultDTO<>(null, 500, "注册失败");
            }
        }
        return new ResultDTO<>(null, 200, "注册成功");
    }

    @Override
    public String generateAndStoreCode(String email) {
        // 检查60秒内是否已发送过验证码
        String cooldownKey = "captcha_cooldown:" + email;
        Boolean hasCooldown = redisTemplate.hasKey(cooldownKey);
        if (hasCooldown) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }
        // 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        // 删除旧验证码（如果存在）
        String captchaKey = "captcha:" + email;
        redisTemplate.delete(captchaKey); // 可选，set操作会自动覆盖
        // 存储新验证码（5分钟过期）
        redisTemplate.opsForValue().set(captchaKey, code, 5, TimeUnit.MINUTES);
        // 设置冷却期标记（60秒过期）
        redisTemplate.opsForValue().set(cooldownKey, "", 60, TimeUnit.SECONDS);
        log.info("验证码已发送到邮箱：{}，验证码：{}", email, code);
        return code;
    }

    @Override
    @Transactional
    public boolean verifyCode(String email, String verificationCode) {
        String redisKey = "captcha:" + email;
        log.info("验证码验证，邮箱：{}，输入的验证码：{}", email, verificationCode);
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        log.info("存储的验证码：{}", storedCode);
        if (storedCode != null && storedCode.equals(verificationCode)) {
            redisTemplate.delete(redisKey); // 验证成功后删除
            return true;
        }
        return false;
    }
}