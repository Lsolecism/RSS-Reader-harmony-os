package org.example.mycute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.FeedBackDTO;
import org.example.mycute.domain.dto.FeedBackGetDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.FeedBack;
import org.example.mycute.mapper.FeedBackMapper;
import org.example.mycute.mapper.UserMapper;
import org.example.mycute.service.FeedBackService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {
    private final FeedBackMapper feedBackMapper;
    private final UserMapper  userMapper;

    @Override
    public Integer getFeedbackCount() {
        return feedBackMapper.selectTotalCount();
    }

    @Override
    public Integer getFeedbackCountByUserId(Integer userId) {
        return feedBackMapper.selectTotalUserCount(userId);
    }

    @Override
    @Transactional
    public ResultDTO<FeedBack> submitFeedback(@NotNull FeedBackDTO feedBackDTO) {
        FeedBack feedBack = new FeedBack(null, feedBackDTO.getFeedbackComments(),
                feedBackDTO.getFeedbackTime(),feedBackDTO.getRateScore());
        log.info("提交反馈{}",feedBack);
        try{
            feedBackMapper.insert(feedBack);
            int insert= userMapper.insertUserFeedback(feedBackDTO.getUserId(), feedBack.getFeedbackId());
            if (insert == 0) {
                return new ResultDTO<>(null, 500, "提交失败");
            }
            return new ResultDTO<>(null, 200, "提交成功");
        }catch (Exception e){
            log.error("提交反馈失败", e);
            return new ResultDTO<>(null, 500, "提交失败");
        }
    }

    @Override
    public ResultDTO<List<FeedBack>> getFeedback(@NotNull FeedBackGetDTO feedBackGetDTO) {
        // 构建分页参数（带默认值保护）
        int pageNum = feedBackGetDTO.getPageNum() != null ? feedBackGetDTO.getPageNum() : 1;
        int pageSize = feedBackGetDTO.getPageSize() != null ? feedBackGetDTO.getPageSize() : 10;
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        if (feedBackGetDTO.getUserId() == null){
            try{
                List<FeedBack> feedBacks = feedBackMapper.findAllFeedbackPage(offset, pageSize);
                return new ResultDTO<>(feedBacks, 200, "获取成功");
            }catch (Exception e)
                {
                return new ResultDTO<>(null, 500, e.toString());
            }
        }
        try{
            List<FeedBack> feedBacks = feedBackMapper.findFeedBackByUserIdPage(feedBackGetDTO.getUserId(), offset, pageSize);
            return new ResultDTO<>(feedBacks, 200, "获取成功");
        }catch (Exception e)
        {
            return new ResultDTO<>(null, 500, e.toString());
        }
    }

}
