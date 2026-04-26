package org.example.mycute.configuration;

import org.example.mycute.domain.dto.LoginFormDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, LoginFormDTO> loginDtoRedisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginFormDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用JSON序列化
        Jackson2JsonRedisSerializer<LoginFormDTO> serializer =
                new Jackson2JsonRedisSerializer<>(LoginFormDTO.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
