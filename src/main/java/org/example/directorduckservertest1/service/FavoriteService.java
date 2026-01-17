package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.dto.*;
import org.example.directorduckservertest1.mapper.FavoriteMapper;
import org.example.directorduckservertest1.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public Result<String> addFavorite(Long userId, String uuid) {
        if (userId == null) return Result.error("userId不能为空");
        if (!StringUtils.hasText(uuid)) return Result.error("题目UUID不能为空");

        // 校验题目存在且status=1（用收藏专用查询）
        QuestionDetailDTO detail = questionMapper.getQuestionDetailForFavoriteByUuid(uuid);
        if (detail == null) return Result.error("题目不存在或已下架");

        int r = favoriteMapper.addFavorite(userId, uuid);
        if (r > 0) return Result.success("收藏成功");
        return Result.success("已收藏");
    }

    public Result<String> removeFavorite(Long userId, String uuid) {
        if (userId == null) return Result.error("userId不能为空");
        if (!StringUtils.hasText(uuid)) return Result.error("题目UUID不能为空");

        int r = favoriteMapper.removeFavorite(userId, uuid);
        if (r > 0) return Result.success("取消收藏成功");
        return Result.success("未收藏（无需取消）");
    }

    public Result<Boolean> exists(Long userId, String uuid) {
        if (userId == null) return Result.error("userId不能为空");
        if (!StringUtils.hasText(uuid)) return Result.error("题目UUID不能为空");

        return Result.success(favoriteMapper.exists(userId, uuid) > 0);
    }

    public Result<FavoritePageDTO<FavoriteQuestionDetailDTO>> listFavorites(Long userId, Integer page, Integer size) {
        if (userId == null) return Result.error("userId不能为空");

        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 50) ? 20 : size;

        long total = favoriteMapper.countByUser(userId);
        int offset = (p - 1) * s;

        List<FavoriteRecordDTO> records = favoriteMapper.listFavoriteRecords(userId, offset, s);
        List<FavoriteQuestionDetailDTO> list = new ArrayList<>();

        // 逐条查题目详情（每页20条，先这样做够用；后续可再做IN批量优化）
        for (FavoriteRecordDTO r : records) {
            QuestionDetailDTO detail = questionMapper.getQuestionDetailForFavoriteByUuid(r.getQuestionUuid());
            if (detail == null) {
                // 题目下架/不存在：这里选择跳过（也可以保留占位）
                continue;
            }

            FavoriteQuestionDetailDTO dto = new FavoriteQuestionDetailDTO();
            dto.setId(detail.getId());
            dto.setUuid(detail.getUuid());
            dto.setQuestionText(detail.getQuestionText());
            dto.setQuestionImage(detail.getQuestionImage());
            dto.setOptionA(detail.getOptionA());
            dto.setOptionB(detail.getOptionB());
            dto.setOptionC(detail.getOptionC());
            dto.setOptionD(detail.getOptionD());

            dto.setCorrectAnswer(detail.getCorrectAnswer());
            dto.setAnalysis(detail.getAnalysis());
            dto.setDifficultyLevel(detail.getDifficultyLevel());
            dto.setStatus(detail.getStatus());
            dto.setCreatedTime(detail.getCreatedTime());
            dto.setUpdatedTime(detail.getUpdatedTime());
            dto.setCategoryName(detail.getCategoryName());

            dto.setFavoritedAt(r.getCreatedAt());
            list.add(dto);
        }

        FavoritePageDTO<FavoriteQuestionDetailDTO> pageDTO = new FavoritePageDTO<>();
        pageDTO.setTotal(total);
        pageDTO.setPage(p);
        pageDTO.setSize(s);
        pageDTO.setList(list);

        return Result.success(pageDTO);
    }
}
