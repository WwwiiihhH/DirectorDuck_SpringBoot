package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.PracticeCommentReq;
import org.example.directorduckservertest1.dto.PracticeCommentResp;
import org.example.directorduckservertest1.service.DeepSeekCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekCommentController {

    private final DeepSeekCommentService deepSeekCommentService;

    public DeepSeekCommentController(DeepSeekCommentService deepSeekCommentService) {
        this.deepSeekCommentService = deepSeekCommentService;
    }

    @PostMapping("/practice-comment")
    public Result<PracticeCommentResp> practiceComment(@RequestBody PracticeCommentReq req) {
        String comment = deepSeekCommentService.generatePracticeComment(req);
        return Result.success(new PracticeCommentResp(comment));
    }
}
