package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.entity.QuestionCategory;
import org.example.directorduckservertest1.entity.QuestionSubcategory;
import org.example.directorduckservertest1.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    // 获取所有大类
    public Result<List<CategoryDTO>> getAllCategories() {
        try {
            List<QuestionCategory> categories = questionMapper.getAllCategories();
            List<CategoryDTO> categoryDTOs = categories.stream().map(category -> {
                CategoryDTO dto = new CategoryDTO();
                dto.setId(category.getId());
                dto.setCategoryCode(category.getCategoryCode());
                dto.setCategoryName(category.getCategoryName());
                return dto;
            }).collect(Collectors.toList());
            return Result.success(categoryDTOs);
        } catch (Exception e) {
            return Result.error("获取题目大类失败：" + e.getMessage());
        }
    }

    // 根据大类获取所有子类
    public Result<List<QuestionSubcategory>> getSubcategoriesByCategoryId(Integer categoryId) {
        try {
            if (categoryId == null) {
                return Result.error("大类ID不能为空");
            }
            List<QuestionSubcategory> subcategories = questionMapper.getSubcategoriesByCategoryId(categoryId);
            return Result.success(subcategories);
        } catch (Exception e) {
            return Result.error("获取子类失败：" + e.getMessage());
        }
    }

    // 根据大类获取题目（简化信息）- 移除limit参数
    public Result<List<QuestionSimpleDTO>> getQuestionsByCategory(Integer categoryId) {
        try {
            if (categoryId == null) {
                return Result.error("大类ID不能为空");
            }

            List<QuestionSimpleDTO> questions = questionMapper.getQuestionsByCategory(categoryId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error("获取题目失败：" + e.getMessage());
        }
    }

    // 根据子类获取题目（简化信息）- 移除limit参数
    public Result<List<QuestionSimpleDTO>> getQuestionsBySubcategory(Integer subcategoryId) {
        try {
            if (subcategoryId == null) {
                return Result.error("子类ID不能为空");
            }

            List<QuestionSimpleDTO> questions = questionMapper.getQuestionsBySubcategory(subcategoryId);
            return Result.success(questions);
        } catch (Exception e) {
            return Result.error("获取题目失败：" + e.getMessage());
        }
    }

    // 根据UUID获取题目详细信息（推荐使用）
    public Result<QuestionDetailDTO> getQuestionDetailByUuid(String uuid) {
        try {
            if (uuid == null || uuid.trim().isEmpty()) {
                return Result.error("题目UUID不能为空");
            }

            QuestionDetailDTO questionDetail = questionMapper.getQuestionDetailByUuid(uuid);
            if (questionDetail == null) {
                return Result.error("题目不存在");
            }

            return Result.success(questionDetail);
        } catch (Exception e) {
            return Result.error("获取题目详情失败：" + e.getMessage());
        }
    }

    // 获取所有题目的UUID列表
    public Result<List<QuestionUuidInfo>> getAllQuestionUuids() {
        try {
            List<QuestionUuidInfo> questionUuids = questionMapper.getAllQuestionUuids();
            return Result.success(questionUuids);
        } catch (Exception e) {
            return Result.error("获取题目UUID列表失败：" + e.getMessage());
        }
    }

    // 保持原有的通过ID查询方法
    public Result<QuestionDetailDTO> getQuestionDetail(Long questionId) {
        try {
            if (questionId == null) {
                return Result.error("题目ID不能为空");
            }

            QuestionDetailDTO questionDetail = questionMapper.getQuestionDetailById(questionId);
            if (questionDetail == null) {
                return Result.error("题目不存在");
            }

            return Result.success(questionDetail);
        } catch (Exception e) {
            return Result.error("获取题目详情失败：" + e.getMessage());
        }
    }

    // 根据UUID获取题目的答案、解析、难度
    public Result<QuestionAnswerDTO> getQuestionAnswerByUuid(String uuid) {
        try {
            if (uuid == null || uuid.trim().isEmpty()) {
                return Result.error("题目UUID不能为空");
            }

            QuestionAnswerDTO questionAnswer = questionMapper.getQuestionAnswerByUuid(uuid);
            if (questionAnswer == null) {
                return Result.error("题目不存在");
            }

            return Result.success(questionAnswer);
        } catch (Exception e) {
            return Result.error("获取题目答案信息失败：" + e.getMessage());
        }
    }

    // 添加题目
    public Result<String> addQuestion(QuestionAddDTO questionAddDTO) {
        try {
            // 参数校验
            if (questionAddDTO == null) {
                return Result.error("题目信息不能为空");
            }
            if (questionAddDTO.getSubcategoryId() == null) {
                return Result.error("子类ID不能为空");
            }
            if (StringUtils.isEmpty(questionAddDTO.getQuestionText())) {
                return Result.error("题目内容不能为空");
            }
            if (StringUtils.isEmpty(questionAddDTO.getOptionA()) ||
                    StringUtils.isEmpty(questionAddDTO.getOptionB()) ||
                    StringUtils.isEmpty(questionAddDTO.getOptionC()) ||
                    StringUtils.isEmpty(questionAddDTO.getOptionD())) {
                return Result.error("四个选项都不能为空");
            }
            if (StringUtils.isEmpty(questionAddDTO.getCorrectAnswer())) {
                return Result.error("正确答案不能为空");
            }
            if (!"A".equals(questionAddDTO.getCorrectAnswer()) &&
                    !"B".equals(questionAddDTO.getCorrectAnswer()) &&
                    !"C".equals(questionAddDTO.getCorrectAnswer()) &&
                    !"D".equals(questionAddDTO.getCorrectAnswer())) {
                return Result.error("正确答案必须是A、B、C、D中的一个");
            }

            // 获取categoryId
            Integer categoryId = questionAddDTO.getCategoryId();
            if (categoryId == null) {
                // 如果没有传categoryId，从subcategory表中获取
                QuestionSubcategory subcategory = questionMapper.getAllSubcategories().stream()
                        .filter(s -> s.getId().equals(questionAddDTO.getSubcategoryId()))
                        .findFirst()
                        .orElse(null);
                if (subcategory != null) {
                    categoryId = subcategory.getCategoryId();
                }
            }

            if (categoryId == null) {
                return Result.error("无法确定题目类别");
            }

            // 根据类别添加题目
            int result = 0;
            switch (categoryId) {
                case 1: // 政治理论
                    result = questionMapper.addPoliticalTheoryQuestion(questionAddDTO);
                    break;
                case 2: // 常识判断
                    result = questionMapper.addCommonSenseQuestion(questionAddDTO);
                    break;
                case 3: // 言语理解与表达
                    result = questionMapper.addLanguageComprehensionQuestion(questionAddDTO);
                    break;
                case 4: // 数量关系
                    result = questionMapper.addQuantitativeRelationQuestion(questionAddDTO);
                    break;
                case 5: // 判断推理
                    result = questionMapper.addReasoningQuestion(questionAddDTO);
                    break;
                case 6: // 资料分析
                    result = questionMapper.addDataAnalysisQuestion(questionAddDTO);
                    break;
                default:
                    return Result.error("不支持的题目类别");
            }

            if (result > 0) {
                return Result.success("题目添加成功");
            } else {
                return Result.error("题目添加失败");
            }
        } catch (Exception e) {
            return Result.error("添加题目失败：" + e.getMessage());
        }
    }

    // 删除题目
    public Result<String> deleteQuestion(QuestionDeleteDTO questionDeleteDTO) {
        try {
            if (questionDeleteDTO == null || StringUtils.isEmpty(questionDeleteDTO.getUuid())) {
                return Result.error("题目UUID不能为空");
            }

            String uuid = questionDeleteDTO.getUuid();

            // 查找题目所在的表
            String tableName = questionMapper.findQuestionTableByUuid(uuid);
            if (tableName == null) {
                return Result.error("题目不存在");
            }

            // 根据表名删除题目
            int result = 0;
            switch (tableName) {
                case "政治理论":
                    result = questionMapper.deletePoliticalTheoryQuestion(uuid);
                    break;
                case "常识判断":
                    result = questionMapper.deleteCommonSenseQuestion(uuid);
                    break;
                case "言语理解与表达":
                    result = questionMapper.deleteLanguageComprehensionQuestion(uuid);
                    break;
                case "数量关系":
                    result = questionMapper.deleteQuantitativeRelationQuestion(uuid);
                    break;
                case "判断推理":
                    result = questionMapper.deleteReasoningQuestion(uuid);
                    break;
                case "资料分析":
                    result = questionMapper.deleteDataAnalysisQuestion(uuid);
                    break;
                default:
                    return Result.error("未知的题目类别");
            }

            if (result > 0) {
                return Result.success("题目删除成功");
            } else {
                return Result.error("题目删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除题目失败：" + e.getMessage());
        }
    }

    // 随机获取题目
    public Result<List<QuestionSimpleDTO>> getRandomQuestions(RandomQuestionRequestDTO requestDTO) {
        try {
            // 参数校验
            if (requestDTO == null) {
                return Result.error("请求参数不能为空");
            }

            Integer count = requestDTO.getCount();
            if (count == null) {
                return Result.error("题目数量不能为空");
            }
            if (count < 5 || count > 20) {
                return Result.error("题目数量必须在5-20之间");
            }

            Integer categoryId = requestDTO.getCategoryId();
            Integer subcategoryId = requestDTO.getSubcategoryId();

            // 必须指定大类或小类中的一个
            if (categoryId == null && subcategoryId == null) {
                return Result.error("必须指定大类ID或小类ID");
            }

            // 不能同时指定大类和小类
            if (categoryId != null && subcategoryId != null) {
                return Result.error("不能同时指定大类ID和小类ID");
            }

            List<QuestionSimpleDTO> questions;

            if (categoryId != null) {
                // 按大类随机获取题目
                questions = questionMapper.getRandomQuestionsByCategory(categoryId, count);
            } else {
                // 按小类随机获取题目
                questions = questionMapper.getRandomQuestionsBySubcategory(subcategoryId, count);
            }

            return Result.success(questions);
        } catch (Exception e) {
            return Result.error("获取随机题目失败：" + e.getMessage());
        }
    }

    // 批量添加题目：默认“有一条失败就整体回滚”
// 如果你想“部分成功部分失败”，我也给你方案（看文末）。
    @Transactional(rollbackFor = Exception.class)
    public Result<BatchAddResultDTO> batchAddQuestions(QuestionBatchAddDTO batchDTO) {
        try {
            if (batchDTO == null || batchDTO.getQuestions() == null || batchDTO.getQuestions().isEmpty()) {
                return Result.error("题目列表不能为空");
            }

            List<QuestionAddDTO> list = batchDTO.getQuestions();
            BatchAddResultDTO resultDTO = new BatchAddResultDTO();
            resultDTO.setTotal(list.size());

            // 1) 预加载所有子类，避免每题都查库
            List<QuestionSubcategory> subs = questionMapper.getAllSubcategories();
            Map<Integer, Integer> subIdToCatId = subs.stream()
                    .collect(Collectors.toMap(QuestionSubcategory::getId, QuestionSubcategory::getCategoryId, (a, b) -> a));

            // 2) 逐条校验 + 自动补全 categoryId + 收集错误
            List<QuestionAddDTO> validated = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                QuestionAddDTO q = list.get(i);

                String err = validateQuestionAddDTO(q);
                if (err != null) {
                    resultDTO.getFailures().add(new BatchAddResultDTO.FailItem(i, err));
                    continue;
                }

                Integer categoryId = q.getCategoryId();
                if (categoryId == null) {
                    Integer cat = subIdToCatId.get(q.getSubcategoryId());
                    if (cat == null) {
                        resultDTO.getFailures().add(new BatchAddResultDTO.FailItem(i, "子类ID不存在：" + q.getSubcategoryId()));
                        continue;
                    }
                    q.setCategoryId(cat);
                } else {
                    // 额外校验：categoryId 与 subcategoryId 是否匹配（可选但很推荐）
                    Integer realCat = subIdToCatId.get(q.getSubcategoryId());
                    if (realCat == null) {
                        resultDTO.getFailures().add(new BatchAddResultDTO.FailItem(i, "子类ID不存在：" + q.getSubcategoryId()));
                        continue;
                    }
                    if (!categoryId.equals(realCat)) {
                        resultDTO.getFailures().add(new BatchAddResultDTO.FailItem(i,
                                "categoryId与subcategoryId不匹配：传入categoryId=" + categoryId + "，实际应为 " + realCat));
                        continue;
                    }
                }

                validated.add(q);
            }

            // 如果有失败：这里选择直接整体失败（回滚更安全）
            if (!resultDTO.getFailures().isEmpty()) {
                resultDTO.setSuccess(0);
                resultDTO.setFail(resultDTO.getFailures().size());
                // 抛异常触发事务回滚（导入一般希望“要么全成要么全不成”）
                throw new RuntimeException("批量导入校验失败，未入库。失败条数=" + resultDTO.getFail());
            }

            // 3) 按 categoryId 分组
            Map<Integer, List<QuestionAddDTO>> group = validated.stream()
                    .collect(Collectors.groupingBy(QuestionAddDTO::getCategoryId));

            int inserted = 0;

            // 4) 分表批量插入
            for (Map.Entry<Integer, List<QuestionAddDTO>> entry : group.entrySet()) {
                Integer catId = entry.getKey();
                List<QuestionAddDTO> qs = entry.getValue();
                if (qs == null || qs.isEmpty()) continue;

                int r;
                switch (catId) {
                    case 1:
                        r = questionMapper.batchAddPoliticalTheoryQuestions(qs);
                        break;
                    case 2:
                        r = questionMapper.batchAddCommonSenseQuestions(qs);
                        break;
                    case 3:
                        r = questionMapper.batchAddLanguageComprehensionQuestions(qs);
                        break;
                    case 4:
                        r = questionMapper.batchAddQuantitativeRelationQuestions(qs);
                        break;
                    case 5:
                        r = questionMapper.batchAddReasoningQuestions(qs);
                        break;
                    case 6:
                        r = questionMapper.batchAddDataAnalysisQuestions(qs);
                        break;
                    default:
                        throw new RuntimeException("不支持的题目类别：" + catId);
                }
                inserted += r;
            }

            resultDTO.setSuccess(inserted);
            resultDTO.setFail(0);
            return Result.success(resultDTO);

        } catch (Exception e) {
            // 事务会回滚
            return Result.error("批量添加题目失败：" + e.getMessage());
        }
    }

    // 复用你的单题校验逻辑：抽成一个方法
    private String validateQuestionAddDTO(QuestionAddDTO q) {
        if (q == null) return "题目信息不能为空";
        if (q.getSubcategoryId() == null) return "子类ID不能为空";
        if (!StringUtils.hasText(q.getQuestionText())) return "题目内容不能为空";

        if (!StringUtils.hasText(q.getOptionA()) ||
                !StringUtils.hasText(q.getOptionB()) ||
                !StringUtils.hasText(q.getOptionC()) ||
                !StringUtils.hasText(q.getOptionD())) {
            return "四个选项都不能为空";
        }

        if (!StringUtils.hasText(q.getCorrectAnswer())) return "正确答案不能为空";

        String ca = q.getCorrectAnswer().trim();
        if (!("A".equals(ca) || "B".equals(ca) || "C".equals(ca) || "D".equals(ca))) {
            return "正确答案必须是A、B、C、D中的一个";
        }

        // 难度可选，但传了就限制范围
        if (q.getDifficultyLevel() != null) {
            byte d = q.getDifficultyLevel();
            if (d < 1 || d > 3) return "difficultyLevel必须是1/2/3";
        }

        return null;
    }


}