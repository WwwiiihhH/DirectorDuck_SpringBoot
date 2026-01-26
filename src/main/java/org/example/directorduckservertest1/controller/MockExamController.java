package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamSessionDTO;
import org.example.directorduckservertest1.service.MockExamSessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模考（用户端）
 * 模考服务：模考场次列表、场次详情查询（用于Android端展示与倒计时）
 *
 * @module 模考模块（用户端）
 */
@RestController
@RequestMapping("/api/mock-exams")
@RequiredArgsConstructor
public class MockExamController {

    private final MockExamSessionService service;

    /**
     * 获取模考场次列表
     * 返回系统内所有模考场次数据（可用于Android端展示“可报名/即将开始/历史场次”等）
     *
     * @return 模考场次列表（MockExamSessionDTO）
     */
    @GetMapping
    public Result<List<MockExamSessionDTO>> list() {
        List<MockExamSessionDTO> list = service.listAll()
                .stream()
                .map(MockExamSessionDTO::fromEntity)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 获取模考场次详情
     * 根据场次ID查询模考场次信息（开考时间、收卷时间、报名截止、状态等）
     *
     * @param id 场次ID
     * @return 模考场次详情（MockExamSessionDTO）
     */
    @GetMapping("/{id}")
    public Result<MockExamSessionDTO> detail(@PathVariable Long id) {
        try {
            return Result.success(MockExamSessionDTO.fromEntity(service.getById(id)));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
