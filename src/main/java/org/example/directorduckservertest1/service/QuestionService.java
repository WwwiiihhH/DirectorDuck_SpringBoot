package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.entity.QuestionCategory;
import org.example.directorduckservertest1.entity.QuestionSubcategory;
import org.example.directorduckservertest1.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

}