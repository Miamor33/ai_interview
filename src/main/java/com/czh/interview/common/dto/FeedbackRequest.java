package com.czh.interview.common.dto;

import lombok.Data;

/**
 * 实时反馈请求
 */
@Data
public class FeedbackRequest {
    /**
     * 面试ID
     */
    private Long interviewId;

    /**
     * 面试题目ID
     */
    private Long interviewItemId;

    /**
     * 当前答案（部分或完整）
     */
    private String currentAnswer;
}
