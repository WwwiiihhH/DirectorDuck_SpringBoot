package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamQuestionDTO;
import org.example.directorduckservertest1.entity.MockExamPaper;
import org.example.directorduckservertest1.service.MockExamPaperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模考试卷（用户端）
 * 试卷管理：生成固定试卷（同场同题，幂等生成）、获取试卷题目列表（不包含正确答案与解析）
 *
 * @module 模考试卷模块
 */
@RestController
@RequestMapping("/api/mock-exams")
@RequiredArgsConstructor
public class MockExamPaperController {

    private final MockExamPaperService paperService;

    /**
     * 生成本场模考试卷（同场同题）
     * 为指定模考场次生成固定试卷，题目从题库随机抽取并固化保存；同一场次仅生成一次（幂等）
     *
     * @param sessionId 模考场次ID
     * @return 试卷信息（MockExamPaper）
     */
    @PostMapping("/{sessionId}/paper/generate")
    public Result<MockExamPaper> generate(@PathVariable Long sessionId) {
        try {
            return Result.success(paperService.generateIfAbsent(sessionId));
        } catch (Exception e) {
            return Result.error("生成试卷失败：" + e.getMessage());
        }
    }

    /**
     * 获取本场模考试卷题目列表
     * 返回固定试卷的题目内容（题干、选项、图片等），不返回正确答案与解析；并按试卷顺序返回
     *
     * @param sessionId 模考场次ID
     * @return 试卷题目列表（MockExamQuestionDTO）
     */
    @GetMapping("/{sessionId}/paper/questions")
    public Result<List<MockExamQuestionDTO>> questions(@PathVariable Long sessionId) {
        try {
            return Result.success(paperService.getPaperQuestions(sessionId));
        } catch (Exception e) {
            return Result.error("获取试卷失败：" + e.getMessage());
        }
    }
}
