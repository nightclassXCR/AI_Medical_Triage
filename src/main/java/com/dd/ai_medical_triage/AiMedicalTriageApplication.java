package com.dd.ai_medical_triage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AiMedicalTriageApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiMedicalTriageApplication.class, args);
        log.info("AI医疗助手启动成功");
    }

}
