package org.example.directorduckservertest1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.MockExamSessionCreateRequest;
import org.example.directorduckservertest1.dto.MockExamSessionDTO;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.service.MockExamSessionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO;


/**
 * 模考（管理员端）
 * 模考管理：创建模考场次、修改模考场次（开考时间、收卷时间、报名截止等）
 *
 * @module 模考管理模块（管理员）
 */
@RestController
@RequestMapping("/api/admin/mock-exams")
@RequiredArgsConstructor
public class AdminMockExamController {

    private final MockExamSessionService service;

    /**
     * 创建模考场次
     * 管理员设置模考场次的基本信息，包括：标题、开考时间、收卷时间、报名截止时间等
     *
     * @param req 创建模考场次请求体（标题/开考时间/收卷时间/报名截止）
     * @return 创建成功后的模考场次信息（MockExamSessionDTO）
     */
    @PostMapping
    public Result<MockExamSessionDTO> create(@RequestBody @Valid MockExamSessionCreateRequest req) {
        try {
            MockExamSession created = service.create(req);
            return Result.success(MockExamSessionDTO.fromEntity(created));
        } catch (Exception e) {
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 修改模考场次
     * 管理员根据场次ID修改模考场次信息（如时间调整、标题更新等）
     *
     * @param id  场次ID
     * @param req 修改模考场次请求体（标题/开考时间/收卷时间/报名截止）
     * @return 修改后的模考场次信息（MockExamSessionDTO）
     */
    @PutMapping("/{id}")
    public Result<MockExamSessionDTO> update(@PathVariable Long id,
                                             @RequestBody @Valid MockExamSessionCreateRequest req) {
        try {
            MockExamSession updated = service.update(id, req);
            return Result.success(MockExamSessionDTO.fromEntity(updated));
        } catch (Exception e) {
            return Result.error("修改失败：" + e.getMessage());
        }
    }



}
