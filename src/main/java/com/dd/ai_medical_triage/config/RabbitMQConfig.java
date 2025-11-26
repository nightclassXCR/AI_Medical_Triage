package com.dd.ai_medical_triage.config;


import com.dd.ai_medical_triage.dto.appointment.AppointmentRequestDTO;
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


    @RabbitListener(queues = REGISTER_QUEUE)
    public void receiveAppointmentMessage(AppointmentRequestDTO message) {
        System.out.println("Received message: " + message);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue appointmentQueue() {
        return QueueBuilder
                .durable(REGISTER_QUEUE)
                .build();
    }

    @Bean
    public Binding appointmentBinding() {
        return BindingBuilder
                .bind(appointmentQueue())
                .to(appointmentExchange())
                .with(ROUTING_KEY_REGISTER);
    }


}
