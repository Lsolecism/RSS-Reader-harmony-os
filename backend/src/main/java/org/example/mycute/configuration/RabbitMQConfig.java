package org.example.mycute.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 从 application.properties 注入配置
    public static final String queueName = "email.queue";
    public static final String exchange =  "email.direct";
    public static final String routingKey = "email.key";


    // 1. 声明队列
    @Bean
    public Queue queue() {
        return new Queue(queueName, true); // true 表示持久化队列
    }

    // 2. 声明直连交换机（Direct Exchange）
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // 3. 绑定队列和交换机
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    // 4. 配置 JSON 消息转换器
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 5. 配置 RabbitTemplate（发送消息用）
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}