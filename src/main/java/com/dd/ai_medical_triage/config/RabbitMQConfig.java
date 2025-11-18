package com.dd.ai_medical_triage.config;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REGISTER_QUEUE = "register.queue";
    public static final String SUMMARY_QUEUE = "ai_summary.queue";

    // TODO: 添加RabbitMQ配置
    @RabbitListener(queues = "queue.name")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }




}
