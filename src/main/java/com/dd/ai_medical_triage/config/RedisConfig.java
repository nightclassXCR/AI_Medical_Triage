package com.dd.ai_medical_triage.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Redis 配置类
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * 配置 Redis 专用的Jackson ObjectMapper
     * 用于（1）处理Java 8时间类型 （2）保留类型 信息，处理对象序列化
     * @return ObjectMapper
     */
    @Bean("redisObjectMapper") // 命名以区分全局 ObjectMapper
    public ObjectMapper objectMapper() {

        // 1. 创建ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // 设置属性可见性
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 设置失败时不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // 2. 处理Java 8时间类型（LocalDateTime、LocalDate、LocalTime)
        JavaTimeModule timeModule = new JavaTimeModule();

        // 自定义LocalDate序列化格式，同时配置序列化器和反序列化器，确保格式一致
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // 自定义LocalTime序列化格式，同时配置序列化器和反序列化器，确保格式一致
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        // 自定义LocalDateTime序列化格式，同时配置序列化器和反序列化器，确保格式一致
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // 注册时间模块到Jackson
        objectMapper.registerModule(timeModule);

        // 禁用将日期序列化为时间戳的行为
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2. 增强类型信息保留（关键：确保接口类型反序列化正确）
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,  // 改为NON_FINAL以确保更多类型被正确处理
                JsonTypeInfo.As.PROPERTY  // 明确指定类型信息作为对象属性存储
        );

        // 3. 注册 Spring AI Message 相关类型（按需添加具体实现类）
        objectMapper.registerSubtypes(
                org.springframework.ai.chat.messages.UserMessage.class,
                org.springframework.ai.chat.messages.AssistantMessage.class,
                org.springframework.ai.chat.messages.SystemMessage.class,
                org.springframework.ai.chat.messages.ToolResponseMessage.class
                // 可根据实际使用的 Message 实现类继续添加
        );

        return objectMapper;
    }

    /**
     * 创建Redis模版对象
     * 将 Redis 默认的 JDK 序列化器替换为 JSON 序列化器（如 Jackson），无需对象实现Serializable，且序列化结果可读性更强
     * @param factory Redis连接工厂对象
     * @return Redis模版对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory,
            @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 配置序列化器（String处理key，Jackson处理value）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
