package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.dto.MockExamQuestionDTO;
import org.example.directorduckservertest1.entity.MockExamPaper;

import java.util.List;

public interface MockExamPaperService {

    /** 幂等生成：同一 session 只生成一次 */
    MockExamPaper generateIfAbsent(Long sessionId);

    /** 获取本场试卷题目（不含答案/解析，按 orderIndex 排序） */
    List<MockExamQuestionDTO> getPaperQuestions(Long sessionId);
}
