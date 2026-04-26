package org.example.mycute.service;

import org.example.mycute.domain.dto.FeedBackDTO;
import org.example.mycute.domain.dto.FeedBackGetDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.FeedBack;

import java.util.List;

public interface FeedBackService {
    ResultDTO<FeedBack> submitFeedback(FeedBackDTO feedBackDTO);
    ResultDTO<List<FeedBack>> getFeedback(FeedBackGetDTO feedBackGetDTO);
    Integer getFeedbackCount();
    Integer getFeedbackCountByUserId(Integer userId);
}
