package com.czh.interview.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提交答案响应
 */
@Data
public class AnswerResponse {
    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 反馈内容
     */
    private String feedback;

    /**
     * 改进建议
     */
    private List<String> suggestions;
}
