package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.MockExamQuestionDTO;
import org.example.directorduckservertest1.entity.MockExamPaper;
import org.example.directorduckservertest1.entity.MockExamPaperQuestion;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.mapper.MockExamQuestionBankMapper;
import org.example.directorduckservertest1.repository.MockExamPaperQuestionRepository;
import org.example.directorduckservertest1.repository.MockExamPaperRepository;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.example.directorduckservertest1.service.MockExamPaperService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MockExamPaperServiceImpl implements MockExamPaperService {

    private final MockExamPaperRepository paperRepo;
    private final MockExamPaperQuestionRepository paperQuestionRepo;
    private final MockExamSessionRepository sessionRepo;
    private final MockExamQuestionBankMapper bankMapper;

    // 固定配比（共130）
    private static final int N_POLITICAL = 20;
    private static final int N_COMMON = 15;
    private static final int N_LANGUAGE = 30;
    private static final int N_QUANT = 10;
    private static final int N_REASONING = 35;
    private static final int N_DATA = 20;

    @Override
    @Transactional
    public MockExamPaper generateIfAbsent(Long sessionId) {
        // 场次存在性校验
        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));

        // 已生成直接返回（幂等）
        Optional<MockExamPaper> existing = paperRepo.findBySessionId(sessionId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 1) 随机抽 uuid
        List<String> political = bankMapper.randomPolitical(N_POLITICAL);
        List<String> common = bankMapper.randomCommon(N_COMMON);
        List<String> language = bankMapper.randomLanguage(N_LANGUAGE);
        List<String> quant = bankMapper.randomQuant(N_QUANT);
        List<String> reasoning = bankMapper.randomReasoning(N_REASONING);
        List<String> data = bankMapper.randomData(N_DATA);

        // 2) 数量校验（题库不足就直接报错）
        ensureEnough("政治理论", political, N_POLITICAL);
        ensureEnough("常识判断", common, N_COMMON);
        ensureEnough("言语理解", language, N_LANGUAGE);
        ensureEnough("数量关系", quant, N_QUANT);
        ensureEnough("判断推理", reasoning, N_REASONING);
        ensureEnough("资料分析", data, N_DATA);

        // 3) 创建 paper（并发下可能唯一键冲突，catch 后读取已有）
        MockExamPaper paper;
        try {
            paper = paperRepo.save(MockExamPaper.builder()
                    .sessionId(sessionId)
                    .totalQuestions(130)
                    .build());
        } catch (DataIntegrityViolationException e) {
            return paperRepo.findBySessionId(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("试卷生成失败：并发冲突"));
        }

        // 4) 固定题目顺序（模块顺序 + 模块内随机顺序）
        int idx = 1;
        List<MockExamPaperQuestion> rows = new ArrayList<>(130);

        idx = append(rows, paper.getId(), sessionId, "POLITICAL", political, idx);
        idx = append(rows, paper.getId(), sessionId, "COMMON", common, idx);
        idx = append(rows, paper.getId(), sessionId, "LANGUAGE", language, idx);
        idx = append(rows, paper.getId(), sessionId, "QUANT", quant, idx);
        idx = append(rows, paper.getId(), sessionId, "REASONING", reasoning, idx);
        append(rows, paper.getId(), sessionId, "DATA", data, idx);

        paperQuestionRepo.saveAll(rows);
        return paper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MockExamQuestionDTO> getPaperQuestions(Long sessionId) {
        // 确保已生成
        MockExamPaper paper = paperRepo.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("本场模考尚未生成试卷"));

        List<MockExamPaperQuestion> pq = paperQuestionRepo.findBySessionIdOrderByOrderIndexAsc(sessionId);
        if (pq.isEmpty()) {
            throw new IllegalArgumentException("试卷题目为空");
        }

        // 按模块分组取题干
        Map<String, List<MockExamPaperQuestion>> byModule = pq.stream()
                .collect(Collectors.groupingBy(MockExamPaperQuestion::getModuleCode));

        Map<String, MockExamQuestionDTO> questionMap = new HashMap<>(pq.size());

        fillMap(questionMap, "POLITICAL", bankMapper.getPoliticalQuestions(extractUuids(byModule.get("POLITICAL"))));
        fillMap(questionMap, "COMMON", bankMapper.getCommonQuestions(extractUuids(byModule.get("COMMON"))));
        fillMap(questionMap, "LANGUAGE", bankMapper.getLanguageQuestions(extractUuids(byModule.get("LANGUAGE"))));
        fillMap(questionMap, "QUANT", bankMapper.getQuantQuestions(extractUuids(byModule.get("QUANT"))));
        fillMap(questionMap, "REASONING", bankMapper.getReasoningQuestions(extractUuids(byModule.get("REASONING"))));
        fillMap(questionMap, "DATA", bankMapper.getDataQuestions(extractUuids(byModule.get("DATA"))));

        // 按 orderIndex 组装返回（保证顺序正确）
        List<MockExamQuestionDTO> result = new ArrayList<>(pq.size());
        for (MockExamPaperQuestion item : pq) {
            MockExamQuestionDTO q = questionMap.get(item.getQuestionUuid());
            if (q != null) {
                q.setModuleCode(item.getModuleCode());
                q.setOrderIndex(item.getOrderIndex());
                result.add(q);
            }
        }
        return result;
    }

    private void ensureEnough(String name, List<String> list, int expected) {
        if (list == null || list.size() < expected) {
            throw new IllegalArgumentException(name + "题库可用题目不足，期望 " + expected + " 道，实际 " +
                    (list == null ? 0 : list.size()) + " 道");
        }
    }

    private int append(List<MockExamPaperQuestion> rows, Long paperId, Long sessionId,
                       String module, List<String> uuids, int startIndex) {
        int idx = startIndex;
        for (String uuid : uuids) {
            rows.add(MockExamPaperQuestion.builder()
                    .paperId(paperId)
                    .sessionId(sessionId)
                    .moduleCode(module)
                    .questionUuid(uuid)
                    .orderIndex(idx++)
                    .build());
        }
        return idx;
    }

    private List<String> extractUuids(List<MockExamPaperQuestion> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(MockExamPaperQuestion::getQuestionUuid).collect(Collectors.toList());
    }

    private void fillMap(Map<String, MockExamQuestionDTO> map, String module, List<MockExamQuestionDTO> list) {
        if (list == null) return;
        for (MockExamQuestionDTO q : list) {
            map.put(q.getUuid(), q);
        }
    }
}
