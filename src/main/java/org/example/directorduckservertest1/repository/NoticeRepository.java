package org.example.directorduckservertest1.repository;

import org.example.directorduckservertest1.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    
    // 按类别查询
    List<Notice> findByCategory(String category);
    
    // 按类别排序查询（最新的在前）
    List<Notice> findByCategoryOrderByPublishTimeDesc(String category);
    
    // 获取所有公告，按发布时间排序
    List<Notice> findAllByOrderByPublishTimeDesc();
    
    // 按标题模糊查询
    List<Notice> findByTitleContainingOrderByPublishTimeDesc(String title);
}