package org.example.directorduckservertest1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.directorduckservertest1.dto.MockExamQuestionDTO;

import java.util.List;

@Mapper
public interface MockExamQuestionBankMapper {

    // --------- 随机抽取 uuid（只抽启用题 status=1） ----------
    @Select("SELECT uuid FROM questions_political_theory WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomPolitical(@Param("limit") int limit);

    @Select("SELECT uuid FROM questions_common_sense WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomCommon(@Param("limit") int limit);

    @Select("SELECT uuid FROM questions_language_comprehension WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomLanguage(@Param("limit") int limit);

    @Select("SELECT uuid FROM questions_quantitative_relation WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomQuant(@Param("limit") int limit);

    @Select("SELECT uuid FROM questions_reasoning WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomReasoning(@Param("limit") int limit);

    @Select("SELECT uuid FROM questions_data_analysis WHERE status=1 ORDER BY RAND() LIMIT #{limit}")
    List<String> randomData(@Param("limit") int limit);

    // --------- 按 uuid 批量取题干（不取正确答案/解析） ----------
    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_political_theory",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getPoliticalQuestions(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_common_sense",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getCommonQuestions(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_language_comprehension",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getLanguageQuestions(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_quantitative_relation",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getQuantQuestions(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_reasoning",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getReasoningQuestions(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, subcategory_id AS subcategoryId, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD",
            "FROM questions_data_analysis",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamQuestionDTO> getDataQuestions(@Param("uuids") List<String> uuids);

    // --------- 错题回看：按 uuid 批量取题干 + 选项 + 解析（analysis） ----------

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_political_theory",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getPoliticalDetails(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_common_sense",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getCommonDetails(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_language_comprehension",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getLanguageDetails(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_quantitative_relation",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getQuantDetails(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_reasoning",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getReasoningDetails(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, question_text AS questionText, question_image AS questionImage,",
            "option_a AS optionA, option_b AS optionB, option_c AS optionC, option_d AS optionD,",
            "analysis AS analysis",
            "FROM questions_data_analysis",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<org.example.directorduckservertest1.dto.MockExamQuestionDetailDTO> getDataDetails(@Param("uuids") List<String> uuids);


}
