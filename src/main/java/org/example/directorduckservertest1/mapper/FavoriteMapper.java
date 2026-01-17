package org.example.directorduckservertest1.mapper;

import org.apache.ibatis.annotations.*;
import org.example.directorduckservertest1.dto.FavoriteRecordDTO;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    // 幂等收藏：已收藏则忽略（依赖 uk_user_question 唯一键）
    @Insert("INSERT IGNORE INTO user_favorite_questions (user_id, question_uuid) VALUES (#{userId}, #{uuid})")
    int addFavorite(@Param("userId") Long userId, @Param("uuid") String uuid);

    @Delete("DELETE FROM user_favorite_questions WHERE user_id = #{userId} AND question_uuid = #{uuid}")
    int removeFavorite(@Param("userId") Long userId, @Param("uuid") String uuid);

    @Select("SELECT COUNT(1) FROM user_favorite_questions WHERE user_id = #{userId} AND question_uuid = #{uuid}")
    int exists(@Param("userId") Long userId, @Param("uuid") String uuid);

    @Select("SELECT COUNT(1) FROM user_favorite_questions WHERE user_id = #{userId}")
    long countByUser(@Param("userId") Long userId);

    @Select("""
            SELECT question_uuid AS questionUuid, created_at AS createdAt
            FROM user_favorite_questions
            WHERE user_id = #{userId}
            ORDER BY created_at DESC
            LIMIT #{size} OFFSET #{offset}
            """)
    List<FavoriteRecordDTO> listFavoriteRecords(@Param("userId") Long userId,
                                                @Param("offset") int offset,
                                                @Param("size") int size);
}
