package com.czh.interview.service.impl;

import com.czh.interview.common.dto.AiEvaluationResult;
import com.czh.interview.common.dto.FeedbackResponse;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.entity.Question;
import com.czh.interview.service.AiEvaluationService;
import com.czh.interview.service.FeedbackService;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 实时反馈服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final AiEvaluationService aiEvaluationService;
    private final InterviewItemService interviewItemService;
    private final QuestionService questionService;

    // 常见技术关键词
    private static final Pattern TECH_KEYWORDS = Pattern.compile(
            "(?i)(java|python|spring|vue|react|mysql|redis|docker|kubernetes|微服务|分布式|算法|数据结构|设计模式)"
    );

    @Override
    public FeedbackResponse getFeedback(Long interviewId, Long interviewItemId, String currentAnswer) {
        FeedbackResponse response = new FeedbackResponse();
        
        if (currentAnswer == null || currentAnswer.trim().isEmpty()) {
            response.setHasUpdate(false);
            response.setText("请开始回答...");
            response.setKeywords(new ArrayList<>());
            response.setSuggestions(List.of("请提供您的答案"));
            return response;
        }

        try {
            // 获取题目信息
            InterviewItem item = interviewItemService.getById(interviewItemId);
            if (item == null) {
                response.setHasUpdate(false);
                response.setText("题目不存在");
                return response;
            }

            Question question = questionService.getById(item.getQuestionId());
            if (question == null) {
                response.setHasUpdate(false);
                response.setText("题目不存在");
                return response;
            }

            // 提取关键词
            List<String> keywords = extractKeywords(currentAnswer);

            // 如果答案长度足够，进行初步评估
            if (currentAnswer.length() > 50) {
                AiEvaluationResult evaluationResult = aiEvaluationService.evaluate(question, currentAnswer);
                
                response.setScore(evaluationResult.getTotalScore());
                response.setText(evaluationResult.getFeedback());
                response.setSuggestions(evaluationResult.getSuggestions());
            } else {
                // 答案太短，给出提示
                response.setText("答案内容较少，建议补充更多细节");
                response.setSuggestions(List.of("可以详细说明技术实现", "可以举例说明", "可以说明应用场景"));
            }

            response.setKeywords(keywords);
            response.setHasUpdate(true);

        } catch (Exception e) {
            log.error("获取实时反馈失败", e);
            response.setHasUpdate(false);
            response.setText("反馈服务暂时不可用");
        }

        return response;
    }

    /**
     * 提取关键词
     */
    private List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        java.util.regex.Matcher matcher = TECH_KEYWORDS.matcher(text);
        while (matcher.find()) {
            String keyword = matcher.group(1);
            if (!keywords.contains(keyword.toLowerCase())) {
                keywords.add(keyword);
            }
        }
        return keywords;
    }
}
