package com.czh.interview.service;

import com.czh.interview.common.dto.FeedbackResponse;

/**
 * 实时反馈服务
 */
public interface FeedbackService {
    /**
     * 获取实时反馈（轮询）
     *
     * @param interviewId 面试ID
     * @param interviewItemId 面试题目ID
     * @param currentAnswer 当前答案
     * @return 反馈响应
     */
    FeedbackResponse getFeedback(Long interviewId, Long interviewItemId, String currentAnswer);
}
