package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.entity.QuestionSubcategory;
import org.example.directorduckservertest1.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库
 * 题库管理：分类、子类、题目查询、详情/答案解析、随机抽题、增删题、批量导入
 *
 * @module 题库模块
 */
@RestController
@RequestMapping("/api/questions")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 获取题目大类
     * 获取全部题目分类（大类）列表
     *
     * @return 大类列表（CategoryDTO）
     */
    @GetMapping("/categories")
    public Result<List<CategoryDTO>> getAllCategories() {
        return questionService.getAllCategories();
    }

    /**
     * 获取题目子类
     * 根据大类ID获取该大类下的全部子类
     *
     * @param categoryId 大类ID
     * @return 子类列表（QuestionSubcategory）
     */
    @GetMapping("/subcategories/{categoryId}")
    public Result<List<QuestionSubcategory>> getSubcategoriesByCategoryId(@PathVariable Integer categoryId) {
        return questionService.getSubcategoriesByCategoryId(categoryId);
    }

    /**
     * 按大类获取题目（简要）
     * 根据大类ID获取题目简要信息列表（不含答案解析）
     *
     * @param categoryId 大类ID
     * @return 题目简要列表（QuestionSimpleDTO）
     */
    @GetMapping("/by-category/{categoryId}")
    public Result<List<QuestionSimpleDTO>> getQuestionsByCategory(@PathVariable Integer categoryId) {
        return questionService.getQuestionsByCategory(categoryId);
    }

    /**
     * 按子类获取题目（简要）
     * 根据子类ID获取题目简要信息列表（不含答案解析）
     *
     * @param subcategoryId 子类ID
     * @return 题目简要列表（QuestionSimpleDTO）
     */
    @GetMapping("/by-subcategory/{subcategoryId}")
    public Result<List<QuestionSimpleDTO>> getQuestionsBySubcategory(@PathVariable Integer subcategoryId) {
        return questionService.getQuestionsBySubcategory(subcategoryId);
    }

    /**
     * 获取题目详情（UUID）
     * 根据题目 UUID 获取题目完整详情（含题干、选项、图片等）
     *
     * @param uuid 题目UUID
     * @return 题目详情（QuestionDetailDTO）
     */
    @GetMapping("/detail/uuid/{uuid}")
    public Result<QuestionDetailDTO> getQuestionDetailByUuid(@PathVariable String uuid) {
        return questionService.getQuestionDetailByUuid(uuid);
    }

    /**
     * 获取题目 UUID 列表
     * 获取所有题目的 UUID 信息（用于客户端缓存/索引）
     *
     * @return UUID 列表（QuestionUuidInfo）
     */
    @GetMapping("/uuids")
    public Result<List<QuestionUuidInfo>> getAllQuestionUuids() {
        return questionService.getAllQuestionUuids();
    }

    /**
     * 获取题目详情（ID）
     * 根据题目ID获取题目完整详情（兼容旧接口）
     *
     * @param questionId 题目ID
     * @return 题目详情（QuestionDetailDTO）
     */
    @GetMapping("/detail/{questionId}")
    public Result<QuestionDetailDTO> getQuestionDetail(@PathVariable Long questionId) {
        return questionService.getQuestionDetail(questionId);
    }

    /**
     * 获取答案与解析（UUID）
     * 根据 UUID 获取正确答案、解析与难度信息
     *
     * @param uuid 题目UUID
     * @return 答案解析信息（QuestionAnswerDTO）
     */
    @GetMapping("/answer/uuid/{uuid}")
    public Result<QuestionAnswerDTO> getQuestionAnswerByUuid(@PathVariable String uuid) {
        return questionService.getQuestionAnswerByUuid(uuid);
    }

    /**
     * 添加题目
     * 新增一道题目（JSON 请求体）
     *
     * @param questionAddDTO 题目新增请求体（题干、选项、答案、解析、分类等）
     * @return 操作结果描述
     */
    @PostMapping("/add")
    public Result<String> addQuestion(@RequestBody QuestionAddDTO questionAddDTO) {
        return questionService.addQuestion(questionAddDTO);
    }

    /**
     * 删除题目
     * 根据请求体指定条件删除题目（JSON 请求体）
     *
     * @param questionDeleteDTO 删除请求体（包含 questionId 或 uuid 等）
     * @return 操作结果描述
     */
    @DeleteMapping("/delete")
    public Result<String> deleteQuestion(@RequestBody QuestionDeleteDTO questionDeleteDTO) {
        return questionService.deleteQuestion(questionDeleteDTO);
    }

    /**
     * 随机抽题
     * 根据分类/子类与数量随机返回题目简要信息列表（JSON 请求体）
     *
     * @param requestDTO 随机抽题请求体（categoryId/subcategoryId/count 等）
     * @return 随机题目列表（QuestionSimpleDTO）
     */
    @PostMapping("/random")
    public Result<List<QuestionSimpleDTO>> getRandomQuestions(@RequestBody RandomQuestionRequestDTO requestDTO) {
        return questionService.getRandomQuestions(requestDTO);
    }

    /**
     * 批量导入题目
     * 批量新增题目数据（JSON 请求体）
     *
     * @param batchDTO 批量导入请求体（questions 列表）
     * @return 批量导入结果（BatchAddResultDTO）
     */
    @PostMapping("/batch-add")
    public Result<BatchAddResultDTO> batchAddQuestions(@RequestBody QuestionBatchAddDTO batchDTO) {
        return questionService.batchAddQuestions(batchDTO);
    }


}