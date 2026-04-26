package org.example.mycute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.configuration.RabbitMQConfig;
import org.example.mycute.service.MQProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class MQProducerServiceImpl implements MQProducerService {


    private final RabbitTemplate rabbitTemplate;

    public void sendEmailTask(String email, String code) {
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("code", code);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.queueName,
                message
        );
    }
}
