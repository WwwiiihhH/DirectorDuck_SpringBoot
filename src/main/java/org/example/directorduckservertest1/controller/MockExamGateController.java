package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamEnterDTO;
import org.example.directorduckservertest1.dto.MockExamStatusDTO;
import org.example.directorduckservertest1.service.MockExamGateService;
import org.springframework.web.bind.annotation.*;

/**
 * 模考候考与开考门禁（用户端）
 * 候考管理：获取服务端时间与场次状态（用于倒计时）、到点后进入考试（服务端统一控制）
 *
 * @module 模考候考模块
 */
@RestController
@RequestMapping("/api/mock-exams")
@RequiredArgsConstructor
public class MockExamGateController {

    private final MockExamGateService gateService;

    /**
     * 获取模考场次状态（用于候考倒计时）
     * 返回服务端当前时间、开考/收卷时间、场次状态、是否已报名、是否允许进入等信息
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数，可选；传入后可返回 joined/canEnter）
     * @return 场次状态信息（MockExamStatusDTO）
     */
    @GetMapping("/{sessionId}/status")
    public Result<MockExamStatusDTO> status(@PathVariable Long sessionId,
                                            @RequestParam(required = false) Long userId) {
        try {
            return Result.success(gateService.getStatus(sessionId, userId));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 进入模考（开考门禁）
     * 服务端校验用户是否已报名、是否到开考时间、是否已超过收卷时间；
     * 通过后返回允许进入（下一步将在此处生成试卷并返回题目列表）
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数）
     * @return 进入结果（MockExamEnterDTO）
     */
    @PostMapping("/{sessionId}/enter")
    public Result<MockExamEnterDTO> enter(@PathVariable Long sessionId,
                                          @RequestParam Long userId) {
        try {
            return Result.success(gateService.enter(sessionId, userId));
        } catch (Exception e) {
            return Result.error("进入失败：" + e.getMessage());
        }
    }
}
