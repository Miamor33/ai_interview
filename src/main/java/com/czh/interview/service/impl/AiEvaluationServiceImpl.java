package com.czh.interview.service.impl;

import com.czh.interview.common.dto.AiEvaluationResult;
import com.czh.interview.config.LocalChatClient;
import com.czh.interview.entity.Question;
import com.czh.interview.service.AiEvaluationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * AI评分服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiEvaluationServiceImpl implements AiEvaluationService {

    private final LocalChatClient localChatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiEvaluationResult evaluate(Question question, String candidateAnswer) {
        if (candidateAnswer == null || candidateAnswer.trim().isEmpty()) {
            return createEmptyResult();
        }

        try {
            // 构建评分Prompt
            String prompt = buildEvaluationPrompt(question, candidateAnswer);
            
            // 调用AI
            String aiResponse = localChatClient.chat(prompt);
            
            // 解析AI返回的JSON
            return parseAiResponse(aiResponse);
        } catch (Exception e) {
            log.error("AI评分失败", e);
            // 失败时返回默认评分
            return createDefaultResult(candidateAnswer);
        }
    }

    /**
     * 构建评分Prompt
     */
    private String buildEvaluationPrompt(Question question, String candidateAnswer) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的面试官，需要对候选人的答案进行评分。\n\n");
        prompt.append("评分维度：\n");
        prompt.append("1. 内容完整性（30分）：答案是否完整回答了题目\n");
        prompt.append("2. 技术准确性（30分）：技术点是否准确\n");
        prompt.append("3. 逻辑清晰度（20分）：表达是否清晰有条理\n");
        prompt.append("4. 深度与广度（20分）：回答的深度和广度\n\n");
        prompt.append("题目：").append(question.getContent()).append("\n\n");
        if (question.getAnswer() != null && !question.getAnswer().isEmpty()) {
            prompt.append("参考答案：").append(question.getAnswer()).append("\n\n");
        }
        prompt.append("候选人答案：").append(candidateAnswer).append("\n\n");
        prompt.append("请按照以下JSON格式返回评分结果（只返回JSON，不要其他文字）：\n");
        prompt.append("{\n");
        prompt.append("  \"totalScore\": 85,\n");
        prompt.append("  \"dimensions\": {\n");
        prompt.append("    \"completeness\": 25,\n");
        prompt.append("    \"accuracy\": 28,\n");
        prompt.append("    \"clarity\": 18,\n");
        prompt.append("    \"depth\": 14\n");
        prompt.append("  },\n");
        prompt.append("  \"feedback\": \"详细的反馈内容，包括优点和不足\",\n");
        prompt.append("  \"suggestions\": [\"建议1\", \"建议2\"]\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    /**
     * 解析AI返回的JSON
     */
    private AiEvaluationResult parseAiResponse(String aiResponse) {
        try {
            // 尝试提取JSON部分（AI可能返回一些额外文字）
            String jsonStr = extractJson(aiResponse);
            
            JsonNode rootNode = objectMapper.readTree(jsonStr);
            
            AiEvaluationResult result = new AiEvaluationResult();
            
            // 解析总分
            if (rootNode.has("totalScore")) {
                result.setTotalScore(BigDecimal.valueOf(rootNode.get("totalScore").asDouble()));
            } else {
                result.setTotalScore(BigDecimal.ZERO);
            }
            
            // 解析各维度评分
            if (rootNode.has("dimensions")) {
                JsonNode dimensionsNode = rootNode.get("dimensions");
                AiEvaluationResult.ScoreDimensions dimensions = new AiEvaluationResult.ScoreDimensions();
                
                if (dimensionsNode.has("completeness")) {
                    dimensions.setCompleteness(BigDecimal.valueOf(dimensionsNode.get("completeness").asDouble()));
                }
                if (dimensionsNode.has("accuracy")) {
                    dimensions.setAccuracy(BigDecimal.valueOf(dimensionsNode.get("accuracy").asDouble()));
                }
                if (dimensionsNode.has("clarity")) {
                    dimensions.setClarity(BigDecimal.valueOf(dimensionsNode.get("clarity").asDouble()));
                }
                if (dimensionsNode.has("depth")) {
                    dimensions.setDepth(BigDecimal.valueOf(dimensionsNode.get("depth").asDouble()));
                }
                
                result.setDimensions(dimensions);
            }
            
            // 解析反馈
            if (rootNode.has("feedback")) {
                result.setFeedback(rootNode.get("feedback").asText());
            }
            
            // 解析建议
            if (rootNode.has("suggestions")) {
                List<String> suggestions = new ArrayList<>();
                for (JsonNode suggestion : rootNode.get("suggestions")) {
                    suggestions.add(suggestion.asText());
                }
                result.setSuggestions(suggestions);
            }
            
            return result;
        } catch (Exception e) {
            log.error("解析AI响应失败: {}", aiResponse, e);
            // 如果解析失败，尝试从文本中提取分数
            return extractScoreFromText(aiResponse);
        }
    }

    /**
     * 从文本中提取JSON
     */
    private String extractJson(String text) {
        // 查找第一个 { 和最后一个 }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    /**
     * 从文本中提取分数（备用方案）
     */
    private AiEvaluationResult extractScoreFromText(String text) {
        AiEvaluationResult result = new AiEvaluationResult();
        
        // 尝试从文本中提取数字作为总分
        String[] parts = text.split("\\D+");
        for (String part : parts) {
            if (!part.isEmpty()) {
                try {
                    double score = Double.parseDouble(part);
                    if (score >= 0 && score <= 100) {
                        result.setTotalScore(BigDecimal.valueOf(score));
                        break;
                    }
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
        }
        
        if (result.getTotalScore() == null) {
            result.setTotalScore(BigDecimal.valueOf(60)); // 默认60分
        }
        
        result.setFeedback(text.length() > 500 ? text.substring(0, 500) : text);
        result.setSuggestions(new ArrayList<>());
        
        return result;
    }

    /**
     * 创建空结果
     */
    private AiEvaluationResult createEmptyResult() {
        AiEvaluationResult result = new AiEvaluationResult();
        result.setTotalScore(BigDecimal.ZERO);
        result.setFeedback("答案为空，无法评分");
        result.setSuggestions(List.of("请提供您的答案"));
        return result;
    }

    /**
     * 创建默认结果（AI调用失败时）
     */
    private AiEvaluationResult createDefaultResult(String candidateAnswer) {
        AiEvaluationResult result = new AiEvaluationResult();
        // 根据答案长度给一个基础分
        int baseScore = Math.min(60 + candidateAnswer.length() / 10, 80);
        result.setTotalScore(BigDecimal.valueOf(baseScore));
        result.setFeedback("AI评分服务暂时不可用，已使用默认评分");
        result.setSuggestions(new ArrayList<>());
        return result;
    }
}
