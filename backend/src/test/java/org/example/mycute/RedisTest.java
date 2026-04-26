package org.example.mycute;

import org.example.mycute.domain.dto.LoginFormDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;


@Slf4j
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, LoginFormDTO> redisTemplate;

    private static final String TEST_KEY = "test:login:dto";

    @AfterEach
    void tearDown() {
        // 每次测试后清理数据
        redisTemplate.delete(TEST_KEY);
    }

    @Test
    void testSaveAndRetrieveLoginDTO() {
        // 1. 构造测试数据
        LoginFormDTO originalDTO = new LoginFormDTO();
        originalDTO.setEmail("test_user_2023");
        originalDTO.setPassWord("securePass123");

        // 2. 存储到Redis
        redisTemplate.opsForValue().set(TEST_KEY, originalDTO);

        // 3. 从Redis获取
        LoginFormDTO retrievedDTO = redisTemplate.opsForValue().get(TEST_KEY);
        log.info("retrievedDTO: {}", retrievedDTO);
    }
}


