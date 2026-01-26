package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.dto.MockExamAnswerKey;
import org.example.directorduckservertest1.dto.MockExamResultDTO;
import org.example.directorduckservertest1.dto.MockExamSubmitRequest;
import org.example.directorduckservertest1.entity.MockExamResult;
import org.example.directorduckservertest1.entity.MockExamSession;
import org.example.directorduckservertest1.mapper.MockExamScoringMapper;
import org.example.directorduckservertest1.repository.MockExamParticipantRepository;
import org.example.directorduckservertest1.repository.MockExamResultRepository;
import org.example.directorduckservertest1.repository.MockExamSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MockExamSubmitServiceImpl implements org.example.directorduckservertest1.service.MockExamSubmitService {

    private final MockExamSessionRepository sessionRepo;
    private final MockExamParticipantRepository participantRepo;
    private final MockExamResultRepository resultRepo;

    private final MockExamScoringMapper scoringMapper;

    // 你 Step3 的题单表 Repository（按你实际类名调整）
    private final org.example.directorduckservertest1.repository.MockExamPaperQuestionRepository paperQuestionRepo;

    private static final int TOTAL = 130;

    private final org.example.directorduckservertest1.repository.MockExamUserAnswerRepository userAnswerRepo;
    private final org.example.directorduckservertest1.mapper.MockExamQuestionBankMapper questionBankMapper;


    @Override
    @Transactional
    public MockExamResultDTO submit(Long sessionId, MockExamSubmitRequest req) {
        // 幂等：已经有成绩直接返回
        Optional<MockExamResult> existing = resultRepo.findBySessionIdAndUserId(sessionId, req.getUserId());
        if (existing.isPresent()) {
            return toDTO(existing.get());
        }

        MockExamSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("模考场次不存在: " + sessionId));

        // 必须报名
        boolean joined = participantRepo.existsBySessionIdAndUserId(sessionId, req.getUserId());
        if (!joined) throw new IllegalArgumentException("未报名，无法交卷");

        // 时间校验：收卷后禁止提交
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(session.getEndTime()) || now.isEqual(session.getEndTime())) {
            throw new IllegalArgumentException("已收卷，无法提交");
        }

        // 取本场固定试卷题单（按 orderIndex 升序）
        var paperQuestions = paperQuestionRepo.findBySessionIdOrderByOrderIndexAsc(sessionId);
        if (paperQuestions == null || paperQuestions.isEmpty()) {
            throw new IllegalArgumentException("本场试卷尚未生成");
        }

        // ✅ 核心：答案数组长度必须与题目数量一致（建议严格校验，避免错位评分）
        if (req.getAnswers() == null) {
            throw new IllegalArgumentException("answers不能为空");
        }
        if (req.getAnswers().size() != paperQuestions.size()) {
            throw new IllegalArgumentException("答案数量不匹配：期望 " + paperQuestions.size() + " 道，实际 " + req.getAnswers().size() + " 道");
        }

        // 按模块分组 uuid（用于批量查正确答案）
        Map<String, List<String>> moduleUuids = paperQuestions.stream()
                .collect(Collectors.groupingBy(
                        pq -> pq.getModuleCode(),
                        Collectors.mapping(pq -> pq.getQuestionUuid(), Collectors.toList())
                ));

        // 拉取正确答案（批量）
        Map<String, String> keyMap = new HashMap<>(paperQuestions.size());
        putKeys(keyMap, scoringMapper.politicalKeys(moduleUuids.getOrDefault("POLITICAL", List.of())));
        putKeys(keyMap, scoringMapper.commonKeys(moduleUuids.getOrDefault("COMMON", List.of())));
        putKeys(keyMap, scoringMapper.languageKeys(moduleUuids.getOrDefault("LANGUAGE", List.of())));
        putKeys(keyMap, scoringMapper.quantKeys(moduleUuids.getOrDefault("QUANT", List.of())));
        putKeys(keyMap, scoringMapper.reasoningKeys(moduleUuids.getOrDefault("REASONING", List.of())));
        putKeys(keyMap, scoringMapper.dataKeys(moduleUuids.getOrDefault("DATA", List.of())));

        // 判分：第 i 个答案对应 orderIndex = i+1 的题
        int correct = 0;
        for (int i = 0; i < paperQuestions.size(); i++) {
            var pq = paperQuestions.get(i);
            String uuid = pq.getQuestionUuid();

            String ua = normalize(req.getAnswers().get(i));     // i 对应 orderIndex=i+1
            String ca = normalize(keyMap.getOrDefault(uuid, ""));

            if (!ua.isEmpty() && ua.equalsIgnoreCase(ca)) {
                correct++;
            }
        }

        double score = calcScore(correct, TOTAL);

        // 如果本场该用户已写入过答案快照，则不重复写（避免唯一键冲突）
        if (!userAnswerRepo.existsBySessionIdAndUserId(sessionId, req.getUserId())) {
            List<org.example.directorduckservertest1.entity.MockExamUserAnswer> rows = new ArrayList<>(paperQuestions.size());

            for (int i = 0; i < paperQuestions.size(); i++) {
                var pq = paperQuestions.get(i);
                String uuid = pq.getQuestionUuid();

                String ua = normalize(req.getAnswers().get(i));
                String ca = normalize(keyMap.getOrDefault(uuid, ""));

                int isCorrect = (!ua.isEmpty() && ua.equalsIgnoreCase(ca)) ? 1 : 0;

                rows.add(org.example.directorduckservertest1.entity.MockExamUserAnswer.builder()
                        .sessionId(sessionId)
                        .userId(req.getUserId())
                        .username(req.getUsername().trim())
                        .orderIndex(pq.getOrderIndex())      // 1..130
                        .moduleCode(pq.getModuleCode())
                        .questionUuid(uuid)
                        .userAnswer(ua)                      // 快照：用户答案
                        .correctAnswer(ca)                   // 快照：正确答案
                        .isCorrect(isCorrect)
                        .build());
            }

            userAnswerRepo.saveAll(rows);
        }


        MockExamResult saved = resultRepo.save(MockExamResult.builder()
                .sessionId(sessionId)
                .userId(req.getUserId())
                .username(req.getUsername().trim())
                .totalQuestions(TOTAL)
                .correctCount(correct)
                .score(score)
                .build());

        return toDTO(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public MockExamResultDTO getResult(Long sessionId, Long userId) {
        MockExamResult r = resultRepo.findBySessionIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("暂无成绩记录"));
        return toDTO(r);
    }

    private void putKeys(Map<String, String> map, List<MockExamAnswerKey> keys) {
        if (keys == null) return;
        for (MockExamAnswerKey k : keys) {
            map.put(k.getUuid(), k.getCorrectAnswer());
        }
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.trim().toUpperCase(Locale.ROOT);
    }

    private double calcScore(int correct, int total) {
        // score = round(correct * 100 / total, 1)
        BigDecimal bd = BigDecimal.valueOf(correct)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 10, RoundingMode.HALF_UP)
                .setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private MockExamResultDTO toDTO(MockExamResult r) {
        return MockExamResultDTO.builder()
                .sessionId(r.getSessionId())
                .userId(r.getUserId())
                .username(r.getUsername())
                .totalQuestions(r.getTotalQuestions())
                .correctCount(r.getCorrectCount())
                .score(r.getScore())
                .submittedAt(r.getSubmittedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO> wrongQuestions(Long sessionId, Long userId) {

        // 先从快照表拿错题（按题号升序）
        var wrongRows = userAnswerRepo.findBySessionIdAndUserIdAndIsCorrectOrderByOrderIndexAsc(sessionId, userId, 0);
        if (wrongRows == null || wrongRows.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // 按模块分组 uuid
        java.util.Map<String, java.util.List<String>> byModule = wrongRows.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        org.example.directorduckservertest1.entity.MockExamUserAnswer::getModuleCode,
                        java.util.stream.Collectors.mapping(org.example.directorduckservertest1.entity.MockExamUserAnswer::getQuestionUuid,
                                java.util.stream.Collectors.toList())
                ));

        // 批量查题干+选项+解析
        java.util.Map<String, org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> detailMap = new java.util.HashMap<>();

        putDetails(detailMap, questionBankMapper.getPoliticalDetails(byModule.getOrDefault("POLITICAL", java.util.List.of())));
        putDetails(detailMap, questionBankMapper.getCommonDetails(byModule.getOrDefault("COMMON", java.util.List.of())));
        putDetails(detailMap, questionBankMapper.getLanguageDetails(byModule.getOrDefault("LANGUAGE", java.util.List.of())));
        putDetails(detailMap, questionBankMapper.getQuantDetails(byModule.getOrDefault("QUANT", java.util.List.of())));
        putDetails(detailMap, questionBankMapper.getReasoningDetails(byModule.getOrDefault("REASONING", java.util.List.of())));
        putDetails(detailMap, questionBankMapper.getDataDetails(byModule.getOrDefault("DATA", java.util.List.of())));

        // 组装返回：以错题快照为准，补全题目详情
        java.util.List<org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO> result = new java.util.ArrayList<>(wrongRows.size());
        for (var r : wrongRows) {
            var d = detailMap.get(r.getQuestionUuid());

            org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO dto = new org.example.directorduckservertest1.dto.MockExamWrongQuestionDTO();
            dto.setOrderIndex(r.getOrderIndex());
            dto.setModuleCode(r.getModuleCode());
            dto.setUserAnswer(r.getUserAnswer());
            dto.setCorrectAnswer(r.getCorrectAnswer());

            if (d != null) {
                dto.setQuestionText(d.getQuestionText());
                dto.setQuestionImage(d.getQuestionImage());
                dto.setOptionA(d.getOptionA());
                dto.setOptionB(d.getOptionB());
                dto.setOptionC(d.getOptionC());
                dto.setOptionD(d.getOptionD());
                dto.setAnalysis(d.getAnalysis());
            } else {
                dto.setAnalysis("题库中未找到该题解析（可能题库已变更）");
            }

            result.add(dto);
        }

        return result;
    }

    private void putDetails(java.util.Map<String, org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> map,
                            java.util.List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> list) {
        if (list == null) return;
        for (var d : list) {
            map.put(d.getUuid(), d);
        }
    }

}
