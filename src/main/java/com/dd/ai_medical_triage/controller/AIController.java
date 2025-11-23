package com.dd.ai_medical_triage.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/ai")
@Tag(
        name = "AI聊天接口",
        description = "提供与AI模型的实时聊天功能，支持流式响应（打字机效果），基于会话ID维护上下文对话"
)
public class AIController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 聊天
     * Flux 是 Spring WebFlux 中的响应式编程类型，表示一个异步的、可能包含多个元素的数据流。
     * 这里用于实现 “流式返回” 效果（类似 ChatGPT 的打字机效果），即后端处理完一部分内容就立即返回一部分，无需等待全部处理完成。
     * @param prompt 用户输入文本
     * @param chatId 会话ID
     * @return 聊天结果
     */
    @RequestMapping(value="/chat",produces = "text/html;charset=utf-8")
    @Operation(
            summary = "AI实时聊天接口",
            description = "与AI模型进行实时对话，支持上下文关联。通过chatId维护会话上下文，返回流式响应实现打字机效果"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "聊天响应成功（流式返回）",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "参数错误（如prompt为空、chatId格式非法）",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "AI服务调用失败或系统异常",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public Flux<String> chat(
            @RequestParam("prompt")
            @Parameter(description = "用户输入的聊天内容，不能为空", required = true)
            String prompt,
            @RequestParam("chatId")
            @Parameter(description = "会话ID，用于关联上下文对话，建议使用UUID格式", required = true)
            String chatId){
        return chatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }
}