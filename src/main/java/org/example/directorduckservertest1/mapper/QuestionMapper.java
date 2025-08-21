package org.example.directorduckservertest1.mapper;

import org.apache.ibatis.annotations.*;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.entity.QuestionCategory;
import org.example.directorduckservertest1.entity.QuestionSubcategory;

import java.util.List;

@Mapper
public interface QuestionMapper {

    // 获取所有大类
    @Select("SELECT id, category_code, category_name, description FROM question_category ORDER BY category_code")
    List<QuestionCategory> getAllCategories();

    // 根据大类获取所有子类
    @Select("SELECT id, category_id, subcategory_code, subcategory_name, description FROM question_subcategory WHERE category_id = #{categoryId}")
    List<QuestionSubcategory> getSubcategoriesByCategoryId(@Param("categoryId") Integer categoryId);

    // 获取所有子类（用于查询）
    @Select("SELECT id, category_id, subcategory_code, subcategory_name FROM question_subcategory")
    List<QuestionSubcategory> getAllSubcategories();

    // 根据大类获取题目（简化信息）- 移除limit
    @Select({
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM (",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_political_theory WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_common_sense WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_language_comprehension WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_quantitative_relation WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_reasoning WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_data_analysis WHERE subcategory_id IN ",
            "(SELECT id FROM question_subcategory WHERE category_id = #{categoryId}) AND status = 1",
            ") AS all_questions"
    })
    List<QuestionSimpleDTO> getQuestionsByCategory(@Param("categoryId") Integer categoryId);

    // 根据子类获取题目（简化信息）- 移除limit
    @Select({
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM (",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_political_theory WHERE subcategory_id = #{subcategoryId} AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_common_sense WHERE subcategory_id = #{subcategoryId} AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_language_comprehension WHERE subcategory_id = #{subcategoryId} AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_quantitative_relation WHERE subcategory_id = #{subcategoryId} AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_reasoning WHERE subcategory_id = #{subcategoryId} AND status = 1",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d FROM questions_data_analysis WHERE subcategory_id = #{subcategoryId} AND status = 1",
            ") AS all_questions"
    })
    List<QuestionSimpleDTO> getQuestionsBySubcategory(@Param("subcategoryId") Integer subcategoryId);

    // 根据UUID获取题目详细信息（推荐使用）
    @Select({
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'政治理论' as category_name FROM questions_political_theory WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'常识判断' as category_name FROM questions_common_sense WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'言语理解与表达' as category_name FROM questions_language_comprehension WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'数量关系' as category_name FROM questions_quantitative_relation WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'判断推理' as category_name FROM questions_reasoning WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'资料分析' as category_name FROM questions_data_analysis WHERE uuid = #{uuid}",
            "LIMIT 1"
    })
    QuestionDetailDTO getQuestionDetailByUuid(@Param("uuid") String uuid);

    // 保持原有的通过ID查询方法（但可能返回多条记录）
    @Select({
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'政治理论' as category_name FROM questions_political_theory WHERE id = #{questionId}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'常识判断' as category_name FROM questions_common_sense WHERE id = #{questionId}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'言语理解与表达' as category_name FROM questions_language_comprehension WHERE id = #{questionId}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'数量关系' as category_name FROM questions_quantitative_relation WHERE id = #{questionId}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'判断推理' as category_name FROM questions_reasoning WHERE id = #{questionId}",
            "UNION ALL",
            "SELECT id, uuid, question_text, question_image, option_a, option_b, option_c, option_d,",
            "correct_answer, analysis, difficulty_level, status, created_time, updated_time,",
            "'资料分析' as category_name FROM questions_data_analysis WHERE id = #{questionId}",
            "LIMIT 1"
    })
    QuestionDetailDTO getQuestionDetailById(@Param("questionId") Long questionId);

    // 获取所有题目的UUID列表（用于前端查询）
    @Select({
            "SELECT uuid, id, '政治理论' as category_name FROM questions_political_theory",
            "UNION ALL",
            "SELECT uuid, id, '常识判断' as category_name FROM questions_common_sense",
            "UNION ALL",
            "SELECT uuid, id, '言语理解与表达' as category_name FROM questions_language_comprehension",
            "UNION ALL",
            "SELECT uuid, id, '数量关系' as category_name FROM questions_quantitative_relation",
            "UNION ALL",
            "SELECT uuid, id, '判断推理' as category_name FROM questions_reasoning",
            "UNION ALL",
            "SELECT uuid, id, '资料分析' as category_name FROM questions_data_analysis",
            "ORDER BY uuid"
    })
    List<QuestionUuidInfo> getAllQuestionUuids();

    // 根据UUID获取题目的答案、解析、难度
    @Select({
            "SELECT uuid, correct_answer, analysis, difficulty_level, '政治理论' as category_name FROM questions_political_theory WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT uuid, correct_answer, analysis, difficulty_level, '常识判断' as category_name FROM questions_common_sense WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT uuid, correct_answer, analysis, difficulty_level, '言语理解与表达' as category_name FROM questions_language_comprehension WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT uuid, correct_answer, analysis, difficulty_level, '数量关系' as category_name FROM questions_quantitative_relation WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT uuid, correct_answer, analysis, difficulty_level, '判断推理' as category_name FROM questions_reasoning WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT uuid, correct_answer, analysis, difficulty_level, '资料分析' as category_name FROM questions_data_analysis WHERE uuid = #{uuid}",
            "LIMIT 1"
    })
    QuestionAnswerDTO getQuestionAnswerByUuid(@Param("uuid") String uuid);

    // 添加题目到政治理论表
    @Insert({
            "INSERT INTO questions_political_theory ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addPoliticalTheoryQuestion(QuestionAddDTO question);

    // 添加题目到常识判断表
    @Insert({
            "INSERT INTO questions_common_sense ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addCommonSenseQuestion(QuestionAddDTO question);

    // 添加题目到言语理解与表达表
    @Insert({
            "INSERT INTO questions_language_comprehension ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addLanguageComprehensionQuestion(QuestionAddDTO question);

    // 添加题目到数量关系表
    @Insert({
            "INSERT INTO questions_quantitative_relation ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addQuantitativeRelationQuestion(QuestionAddDTO question);

    // 添加题目到判断推理表
    @Insert({
            "INSERT INTO questions_reasoning ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addReasoningQuestion(QuestionAddDTO question);

    // 添加题目到资料分析表
    @Insert({
            "INSERT INTO questions_data_analysis ",
            "(subcategory_id, question_text, question_image, option_a, option_b, option_c, option_d, ",
            "correct_answer, analysis, difficulty_level, uuid) ",
            "VALUES ",
            "(#{subcategoryId}, #{questionText}, #{questionImage}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, ",
            "#{correctAnswer}, #{analysis}, #{difficultyLevel}, UUID())"
    })

    int addDataAnalysisQuestion(QuestionAddDTO question);

    // 根据UUID删除政治理论题目
    @Delete("DELETE FROM questions_political_theory WHERE uuid = #{uuid}")
    int deletePoliticalTheoryQuestion(@Param("uuid") String uuid);

    // 根据UUID删除常识判断题目
    @Delete("DELETE FROM questions_common_sense WHERE uuid = #{uuid}")
    int deleteCommonSenseQuestion(@Param("uuid") String uuid);

    // 根据UUID删除言语理解与表达题目
    @Delete("DELETE FROM questions_language_comprehension WHERE uuid = #{uuid}")
    int deleteLanguageComprehensionQuestion(@Param("uuid") String uuid);

    // 根据UUID删除数量关系题目
    @Delete("DELETE FROM questions_quantitative_relation WHERE uuid = #{uuid}")
    int deleteQuantitativeRelationQuestion(@Param("uuid") String uuid);

    // 根据UUID删除判断推理题目
    @Delete("DELETE FROM questions_reasoning WHERE uuid = #{uuid}")
    int deleteReasoningQuestion(@Param("uuid") String uuid);

    // 根据UUID删除资料分析题目
    @Delete("DELETE FROM questions_data_analysis WHERE uuid = #{uuid}")
    int deleteDataAnalysisQuestion(@Param("uuid") String uuid);

    // 根据UUID查找题目所在表（用于删除前确认）
    @Select({
            "SELECT '政治理论' as table_name FROM questions_political_theory WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT '常识判断' as table_name FROM questions_common_sense WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT '言语理解与表达' as table_name FROM questions_language_comprehension WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT '数量关系' as table_name FROM questions_quantitative_relation WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT '判断推理' as table_name FROM questions_reasoning WHERE uuid = #{uuid}",
            "UNION ALL",
            "SELECT '资料分析' as table_name FROM questions_data_analysis WHERE uuid = #{uuid}",
            "LIMIT 1"
    })
    String findQuestionTableByUuid(@Param("uuid") String uuid);


}