package com.dd.ai_medical_triage.controller;

import com.dd.ai_medical_triage.annatation.LoginRequired;
import com.dd.ai_medical_triage.dto.PageResult;
import com.dd.ai_medical_triage.dto.chat.ChatPromptDTO;
import com.dd.ai_medical_triage.dto.chat.ChatSessionDetailDTO;
import com.dd.ai_medical_triage.dto.chat.ChatSessionListItem;
import com.dd.ai_medical_triage.dto.chat.ChatSessionQueryDTO;
import com.dd.ai_medical_triage.service.base.ChatSessionService;
import com.dd.ai_medical_triage.utils.RequestParseUtil;
import com.dd.ai_medical_triage.vo.ResultVO;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private RequestParseUtil requestParseUtil;

    @Autowired
    private ChatSessionService chatSessionService;

    /**
     * AI 聊天会话创建ID接口
     */
    @GetMapping("/chat/create")
    @LoginRequired
    @Operation(
            summary = "创建会话ID接口",
            description = "创建一个会话ID，用于后续的会话上下文管理"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "会话ID创建成功",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "未登录（对应错误码：SYSTEM_021）",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "500", description = "系统异常",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public String createChat(){
        Long userId = parseUserIdFromToken();
        return chatSessionService.createSessionId(userId);
    }

    /**
     * AI 聊天会话接口
     * Flux 是 Spring WebFlux 中的响应式编程类型，表示一个异步的、可能包含多个元素的数据流。
     * 这里用于实现 “流式返回” 效果（类似 ChatGPT 的打字机效果），即后端处理完一部分内容就立即返回一部分，无需等待全部处理完成。
     */
    @PostMapping(value="/chat",produces = "text/html;charset=utf-8")
    @LoginRequired
    @Operation(
            summary = "AI实时聊天接口",
            description = "与AI模型进行实时对话，支持上下文关联。通过chatId维护会话上下文，返回流式响应实现打字机效果"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "聊天响应成功（流式返回）",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "参数错误（如prompt为空、chatId格式非法）",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "未登录（对应错误码：SYSTEM_021）",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "500", description = "AI服务调用失败或系统异常",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public Flux<String> chat(
            @Valid @RequestBody
            @Parameter(description = "AI对话参数，含用户名、手机号/邮箱、密码、验证码", required = true)
            ChatPromptDTO prompt){
        return chatClient.prompt()
                .user(prompt.getPrompt())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,prompt.getChatSessionId()))
                .stream()
                .content();
    }

    /**
     * 获取会话详情接口
     */
    @GetMapping("/chat/detail")
    @LoginRequired
    @Operation(
            summary = "获取会话详情",
            description = "查询指定会话的完整内容，包括所有消息记录"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "会话详情",
                    content = @Content(schema = @Schema(implementation = ChatSessionDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "未登录",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "404", description = "会话不存在",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "500", description = "系统异常",
                    content = @Content(schema = @Schema(implementation = ResultVO.class)))
    })
    public ChatSessionDetailDTO getSessionDetail(
            @Parameter(description = "会话ID", required = true) @RequestParam String sessionId) {
        Long userId = parseUserIdFromToken();
        return chatSessionService.selectSessionById(sessionId, userId);
    }

    /**
     * 分页查询会话列表
     */
    @PostMapping("/chat/list")
    @LoginRequired
    @Operation(
            summary = "查询会话列表",
            description = "分页查询当前用户的会话列表，支持条件筛选"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "会话列表",
                    content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "401", description = "未登录",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "500", description = "系统异常",
                    content = @Content(schema = @Schema(implementation = ResultVO.class)))
    })
    public PageResult<ChatSessionListItem> queryPrivateSessionList(
            @Valid @RequestBody
            @Parameter(description = "查询参数（含分页和筛选条件）", required = true)
            ChatSessionQueryDTO queryDTO) {
        // 设置当前登录用户ID作为查询条件（数据隔离）
        queryDTO.setUserId(parseUserIdFromToken());
        return chatSessionService.querySessions(queryDTO);
    }

    /**
     * 查询会话总数
     */
    @PostMapping("/chat/count")
    @LoginRequired
    @Operation(
            summary = "查询会话数量",
            description = "根据条件查询当前用户的会话总数"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "会话数量",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "未登录",
                    content = @Content(schema = @Schema(implementation = ResultVO.class))),
            @ApiResponse(responseCode = "500", description = "系统异常",
                    content = @Content(schema = @Schema(implementation = ResultVO.class)))
    })
    public int queryPrivateSessionCount(
            @Valid @RequestBody
            @Parameter(description = "查询参数", required = true)
            ChatSessionQueryDTO queryDTO) {
        queryDTO.setUserId(parseUserIdFromToken());
        return chatSessionService.countSessions(queryDTO);
    }

    /**
     * 工具方法：从请求头令牌中解析用户ID（实际项目需结合JWT工具实现）
     * @return 当前登录用户ID
     */
    private Long parseUserIdFromToken() {
        // 通过HttpServletRequest获取Authorization头，解析JWT令牌得到用户ID
        return requestParseUtil.parseUserIdFromRequest();
    }
}