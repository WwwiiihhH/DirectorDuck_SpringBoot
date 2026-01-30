package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 根据帖子ID和用户ID查找点赞记录
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    // 统计某个帖子的点赞数
    Long countByPostId(Long postId);

    // 查询某个用户点赞的所有帖子ID
    @Query("SELECT pl.postId FROM PostLike pl WHERE pl.userId = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") Long userId);

    // 删除点赞记录
    void deleteByPostIdAndUserId(Long postId, Long userId);

    // 删除指定帖子所有点赞（用于帖子删除）
    void deleteByPostId(Long postId);

    // 检查用户是否已点赞某个帖子
    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
