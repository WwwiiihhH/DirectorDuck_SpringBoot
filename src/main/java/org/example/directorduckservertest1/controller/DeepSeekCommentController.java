package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.PracticeCommentReq;
import org.example.directorduckservertest1.dto.PracticeCommentResp;
import org.example.directorduckservertest1.service.DeepSeekCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AI
 * AI 学习点评：根据刷题数据生成点评内容
 *
 * @module AI模块
 */
@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekCommentController {

    private final DeepSeekCommentService deepSeekCommentService;

    public DeepSeekCommentController(DeepSeekCommentService deepSeekCommentService) {
        this.deepSeekCommentService = deepSeekCommentService;
    }

    /**
     * 生成刷题点评
     * 根据用户刷题表现数据生成 AI 点评（JSON 请求体）
     *
     * @param req 刷题点评请求体（例如：做题数量、正确率、耗时、薄弱点等）
     * @return 点评结果（PracticeCommentResp）
     */
    @PostMapping("/practice-comment")
    public Result<PracticeCommentResp> practiceComment(@RequestBody PracticeCommentReq req) {
        String comment = deepSeekCommentService.generatePracticeComment(req);
        return Result.success(new PracticeCommentResp(comment));
    }
}
