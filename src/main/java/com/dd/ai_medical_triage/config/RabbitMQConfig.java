package com.dd.ai_medical_triage.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "healthcare.exchange";

    public static final String ROUTING_KEY_REGISTER = "register.request";
    public static final String ROUTING_KEY_SUMMARY = "summary.ai";

    public static final String REGISTER_QUEUE = "register.queue";
    public static final String SUMMARY_QUEUE = "ai_summary.queue";



    // TODO: 添加RabbitMQ配置
    @RabbitListener(queues = SUMMARY_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange appointmentExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue appointmentQueue() {
        return QueueBuilder
                .durable(SUMMARY_QUEUE)
                .build();
    }

    @Bean
    public Binding appointmentBinding() {
        return BindingBuilder
                .bind(appointmentQueue())
                .to(appointmentExchange())
                .with(ROUTING_KEY_SUMMARY);
    }


}
