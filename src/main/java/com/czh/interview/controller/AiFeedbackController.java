package com.czh.interview.controller;

import com.czh.interview.common.R;
import com.czh.interview.common.dto.FeedbackRequest;
import com.czh.interview.common.dto.FeedbackResponse;
import com.czh.interview.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * AI实时反馈控制器
 */
@RestController
@RequestMapping("/api/ai/feedback")
@RequiredArgsConstructor
public class AiFeedbackController {

    private final FeedbackService feedbackService;

    /**
     * 轮询获取实时反馈
     */
    @PostMapping("/poll")
    public R<FeedbackResponse> pollFeedback(@RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.getFeedback(
                request.getInterviewId(),
                request.getInterviewItemId(),
                request.getCurrentAnswer()
        );
        return R.ok(response);
    }
}
