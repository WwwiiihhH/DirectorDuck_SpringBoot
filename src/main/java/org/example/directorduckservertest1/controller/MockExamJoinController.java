package org.example.directorduckservertest1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamJoinRequest;
import org.example.directorduckservertest1.entity.MockExamParticipant;
import org.example.directorduckservertest1.service.MockExamParticipantService;
import org.springframework.web.bind.annotation.*;

/**
 * 模考报名（用户端）
 * 报名管理：报名参加模考、取消报名、查询是否已报名、查询报名人数
 *
 * @module 模考报名模块
 */
@RestController
@RequestMapping("/api/mock-exams")
@RequiredArgsConstructor
public class MockExamJoinController {

    private final MockExamParticipantService participantService;

    /**
     * 报名参加模考
     * 用户报名指定模考场次，报名成功后写入报名记录（同一用户同一场次不可重复报名）
     *
     * @param sessionId 模考场次ID
     * @param req 报名请求体（userId、username）
     * @return 报名结果（报名记录）
     */
    @PostMapping("/{sessionId}/join")
    public Result<MockExamParticipant> join(@PathVariable Long sessionId,
                                           @RequestBody @Valid MockExamJoinRequest req) {
        try {
            return Result.success(participantService.join(sessionId, req));
        } catch (Exception e) {
            return Result.error("报名失败：" + e.getMessage());
        }
    }

    /**
     * 取消报名
     * 用户取消指定模考场次的报名记录（开考后可禁止取消）
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数）
     * @return 操作结果
     */
    @DeleteMapping("/{sessionId}/join")
    public Result<String> cancel(@PathVariable Long sessionId,
                                 @RequestParam Long userId) {
        try {
            participantService.cancel(sessionId, userId);
            return Result.success("取消报名成功");
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 查询是否已报名
     * 判断用户是否已报名某场模考（用于Android端按钮状态显示）
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数）
     * @return 是否已报名（true/false）
     */
    @GetMapping("/{sessionId}/join/exists")
    public Result<Boolean> exists(@PathVariable Long sessionId,
                                  @RequestParam Long userId) {
        try {
            return Result.success(participantService.exists(sessionId, userId));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取报名人数
     * 返回某场模考当前报名总人数
     *
     * @param sessionId 模考场次ID
     * @return 报名人数
     */
    @GetMapping("/{sessionId}/participants/count")
    public Result<Long> count(@PathVariable Long sessionId) {
        try {
            return Result.success(participantService.countParticipants(sessionId));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
