package org.example.mycute.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.dto.FeedBackDTO;
import org.example.mycute.domain.dto.FeedBackGetDTO;
import org.example.mycute.domain.dto.ResultDTO;
import org.example.mycute.domain.entity.FeedBack;
import org.example.mycute.service.FeedBackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping("/submit")
    public ResultDTO<FeedBack> submitFeedback(@Valid @RequestBody FeedBackDTO feedBackDTO) {
        return feedBackService.submitFeedback(feedBackDTO);
    }

    @PostMapping("/getFeedback")
    public ResultDTO<List<FeedBack>> getFeedback(@Valid @RequestBody FeedBackGetDTO feedBackGetDTO) {
        return feedBackService.getFeedback(feedBackGetDTO);
    }

    @PostMapping("/getFeedbackNumberByUserId")
    public Integer getFeedbackNumberByUserId(@Valid @RequestBody FeedBackGetDTO feedBackGetDTO) {
        return feedBackService.getFeedbackCountByUserId(feedBackGetDTO.getUserId());
    }

    @PostMapping("/getFeedbackNumber")
    public Integer getFeedbackNumber() {
        return feedBackService.getFeedbackCount();
    }
}
