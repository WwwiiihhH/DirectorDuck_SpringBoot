package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.DeepSeekDtos;
import org.example.directorduckservertest1.service.DeepSeekService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    @PostMapping("/chat")
    public Result<DeepSeekDtos.ProxyChatResponse> chat(@RequestBody DeepSeekDtos.ProxyChatRequest req) {
        try {
            return Result.success(deepSeekService.chat(req));
        } catch (Exception e) {
            return Result.error(e.getMessage() == null ? "DeepSeek 调用失败" : e.getMessage());
        }
    }
}
