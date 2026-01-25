package com.czh.interview.service;

import com.czh.interview.common.dto.AiEvaluationResult;
import com.czh.interview.entity.Question;

/**
 * AI评分服务
 */
public interface AiEvaluationService {
    /**
     * 对答案进行AI评分
     *
     * @param question 题目
     * @param candidateAnswer 候选人答案
     * @return 评分结果
     */
    AiEvaluationResult evaluate(Question question, String candidateAnswer);
}
