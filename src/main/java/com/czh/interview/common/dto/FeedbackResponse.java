package com.czh.interview.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 实时反馈响应
 */
@Data
public class FeedbackResponse {
    /**
     * 反馈文本
     */
    private String text;

    /**
     * 当前评分（如果有）
     */
    private BigDecimal score;

    /**
     * 识别的关键词
     */
    private List<String> keywords;

    /**
     * 建议
     */
    private List<String> suggestions;

    /**
     * 是否有更新
     */
    private Boolean hasUpdate;
}
