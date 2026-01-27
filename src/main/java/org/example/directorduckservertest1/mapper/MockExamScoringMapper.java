package org.example.directorduckservertest1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.directorduckservertest1.dto.MockExamAnswerKey;

import java.util.List;

@Mapper
public interface MockExamScoringMapper {

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_political_theory",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> politicalKeys(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_common_sense",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> commonKeys(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_language_comprehension",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> languageKeys(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_quantitative_relation",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> quantKeys(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_reasoning",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> reasoningKeys(@Param("uuids") List<String> uuids);

    @Select({
            "<script>",
            "SELECT uuid, correct_answer AS correctAnswer FROM questions_data_analysis",
            "WHERE uuid IN",
            "<foreach collection='uuids' item='u' open='(' separator=',' close=')'>#{u}</foreach>",
            "</script>"
    })
    List<MockExamAnswerKey> dataKeys(@Param("uuids") List<String> uuids);

    /**
     * 查询某用户在某场模考是否有结果记录
     */
    @Select("SELECT COUNT(*) FROM mock_exam_result WHERE session_id = #{sessionId} AND user_id = #{userId}")
    int countBySessionAndUser(@Param("sessionId") Long sessionId, @Param("userId") Long userId);
}
