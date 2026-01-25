package com.czh.interview.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * AI评分结果
 */
@Data
public class AiEvaluationResult {
    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 各维度评分
     */
    private ScoreDimensions dimensions;

    /**
     * 反馈内容
     */
    private String feedback;

    /**
     * 改进建议
     */
    private List<String> suggestions;

    @Data
    public static class ScoreDimensions {
        /**
         * 内容完整性（30分）
         */
        private BigDecimal completeness;

        /**
         * 技术准确性（30分）
         */
        private BigDecimal accuracy;

        /**
         * 逻辑清晰度（20分）
         */
        private BigDecimal clarity;

        /**
         * 深度与广度（20分）
         */
        private BigDecimal depth;
    }
}
