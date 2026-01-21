package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.DeepSeekDtos;
import org.example.directorduckservertest1.service.DeepSeekService;
import org.springframework.web.bind.annotation.*;

/**
 * AI
 * DeepSeek 对话代理：将客户端请求转发至 DeepSeek 服务并返回结果
 *
 * @module AI模块
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    private final DeepSeekService deepSeekService;


    /**
     * DeepSeek 对话
     * 代理转发聊天请求（JSON 请求体），返回模型回复内容
     *
     * @param req 代理聊天请求体（包含 messages、模型参数等）
     * @return 代理聊天响应（ProxyChatResponse）
     */
    @PostMapping("/chat")
    public Result<DeepSeekDtos.ProxyChatResponse> chat(@RequestBody DeepSeekDtos.ProxyChatRequest req) {
        try {
            return Result.success(deepSeekService.chat(req));
        } catch (Exception e) {
            return Result.error(e.getMessage() == null ? "DeepSeek 调用失败" : e.getMessage());
        }
    }
}
