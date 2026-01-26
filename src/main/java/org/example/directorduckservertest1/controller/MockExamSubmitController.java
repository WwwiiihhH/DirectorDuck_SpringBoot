package org.example.directorduckservertest1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamResultDTO;
import org.example.directorduckservertest1.dto.MockExamSubmitRequest;
import org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO;
import org.example.directorduckservertest1.service.MockExamSubmitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模考交卷与成绩（用户端）
 * 交卷管理：提交本场答案并判分、查询本场成绩结果
 *
 * @module 模考判分模块
 */
@RestController
@RequestMapping("/api/mock-exams")
@RequiredArgsConstructor
public class MockExamSubmitController {

    private final MockExamSubmitService submitService;

    /**
     * 提交答案并交卷判分
     * 用户按试卷顺序提交答案列表（不需要传uuid），服务端按本场固定试卷题单（orderIndex顺序）匹配正确答案判分并保存成绩
     * 计分规则：score = round(correctCount * 100 / 130, 1)
     *
     * @param sessionId 模考场次ID
     * @param req 交卷请求体（userId、username、answers列表：按题目顺序 ["A","B","","C"...]）
     * @return 成绩结果（MockExamResultDTO）
     */
    @PostMapping("/{sessionId}/submit")
    public Result<MockExamResultDTO> submit(@PathVariable Long sessionId,
                                            @RequestBody @Valid MockExamSubmitRequest req) {
        try {
            return Result.success(submitService.submit(sessionId, req));
        } catch (Exception e) {
            return Result.error("交卷失败：" + e.getMessage());
        }
    }


    /**
     * 查询本场成绩
     * 根据场次ID与用户ID查询该用户的交卷成绩（若未交卷则返回错误）
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数）
     * @return 成绩结果（MockExamResultDTO）
     */
    @GetMapping("/{sessionId}/result")
    public Result<MockExamResultDTO> result(@PathVariable Long sessionId,
                                           @RequestParam Long userId) {
        try {
            return Result.success(submitService.getResult(sessionId, userId));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取错题详情列表
     * 根据本场交卷快照（mock_exam_user_answer）查询错题，并返回错题的题干、选项、用户选项、正确选项、解析等信息
     *
     * @param sessionId 模考场次ID
     * @param userId 用户ID（query参数）
     * @return 错题详情列表（MockExamWrongQuestionDTO）
     */
    @GetMapping("/{sessionId}/wrong-questions")
    public Result<List<MockExamWrongQuestionDTO>> wrongQuestions(@PathVariable Long sessionId,
                                                                 @RequestParam Long userId) {
        try {
            return Result.success(submitService.wrongQuestions(sessionId, userId));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
