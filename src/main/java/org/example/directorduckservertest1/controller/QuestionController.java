package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.entity.QuestionSubcategory;
import org.example.directorduckservertest1.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // 获取所有大类
    @GetMapping("/categories")
    public Result<List<CategoryDTO>> getAllCategories() {
        return questionService.getAllCategories();
    }

    // 根据大类获取所有子类
    @GetMapping("/subcategories/{categoryId}")
    public Result<List<QuestionSubcategory>> getSubcategoriesByCategoryId(@PathVariable Integer categoryId) {
        return questionService.getSubcategoriesByCategoryId(categoryId);
    }

    // 根据大类获取题目（简化信息）- 移除limit参数
    @GetMapping("/by-category/{categoryId}")
    public Result<List<QuestionSimpleDTO>> getQuestionsByCategory(@PathVariable Integer categoryId) {
        return questionService.getQuestionsByCategory(categoryId);
    }

    // 根据子类获取题目（简化信息）- 移除limit参数
    @GetMapping("/by-subcategory/{subcategoryId}")
    public Result<List<QuestionSimpleDTO>> getQuestionsBySubcategory(@PathVariable Integer subcategoryId) {
        return questionService.getQuestionsBySubcategory(subcategoryId);
    }

    // 根据UUID获取题目详细信息（推荐使用）
    @GetMapping("/detail/uuid/{uuid}")
    public Result<QuestionDetailDTO> getQuestionDetailByUuid(@PathVariable String uuid) {
        return questionService.getQuestionDetailByUuid(uuid);
    }

    // 获取所有题目的UUID列表
    @GetMapping("/uuids")
    public Result<List<QuestionUuidInfo>> getAllQuestionUuids() {
        return questionService.getAllQuestionUuids();
    }

    // 保持原有的通过ID查询方法
    @GetMapping("/detail/{questionId}")
    public Result<QuestionDetailDTO> getQuestionDetail(@PathVariable Long questionId) {
        return questionService.getQuestionDetail(questionId);
    }

    // 根据UUID获取题目的答案、解析、难度
    @GetMapping("/answer/uuid/{uuid}")
    public Result<QuestionAnswerDTO> getQuestionAnswerByUuid(@PathVariable String uuid) {
        return questionService.getQuestionAnswerByUuid(uuid);
    }

    // 添加题目
    @PostMapping("/add")
    public Result<String> addQuestion(@RequestBody QuestionAddDTO questionAddDTO) {
        return questionService.addQuestion(questionAddDTO);
    }

    // 删除题目
    @DeleteMapping("/delete")
    public Result<String> deleteQuestion(@RequestBody QuestionDeleteDTO questionDeleteDTO) {
        return questionService.deleteQuestion(questionDeleteDTO);
    }

    // 随机获取题目
    @PostMapping("/random")
    public Result<List<QuestionSimpleDTO>> getRandomQuestions(@RequestBody RandomQuestionRequestDTO requestDTO) {
        return questionService.getRandomQuestions(requestDTO);
    }

}