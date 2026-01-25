package com.czh.interview.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.interview.common.R;
import com.czh.interview.entity.Interview;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计分析控制器
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final InterviewService interviewService;
    private final InterviewItemService interviewItemService;

    /**
     * 获取用户统计数据
     */
    @GetMapping("/user/{userId}")
    public R<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 查询该用户的所有面试
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getCandidateId, userId);
        List<Interview> interviews = interviewService.list(wrapper);

        // 面试次数
        result.put("totalInterviews", interviews.size());

        // 已完成面试次数
        long completedCount = interviews.stream()
                .filter(i -> i.getStatus() != null && i.getStatus() == 1)
                .count();
        result.put("completedInterviews", completedCount);

        // 平均分
        double avgScore = interviews.stream()
                .filter(i -> i.getScore() != null)
                .mapToDouble(i -> i.getScore().doubleValue())
                .average()
                .orElse(0.0);
        result.put("averageScore", BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP));

        // 最高分
        double maxScore = interviews.stream()
                .filter(i -> i.getScore() != null)
                .mapToDouble(i -> i.getScore().doubleValue())
                .max()
                .orElse(0.0);
        result.put("maxScore", BigDecimal.valueOf(maxScore).setScale(2, RoundingMode.HALF_UP));

        // 最低分
        double minScore = interviews.stream()
                .filter(i -> i.getScore() != null)
                .mapToDouble(i -> i.getScore().doubleValue())
                .min()
                .orElse(0.0);
        result.put("minScore", BigDecimal.valueOf(minScore).setScale(2, RoundingMode.HALF_UP));

        // 获取所有面试题目统计
        List<Long> interviewIds = interviews.stream()
                .map(Interview::getId)
                .toList();
        
        if (!interviewIds.isEmpty()) {
            LambdaQueryWrapper<InterviewItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.in(InterviewItem::getInterviewId, interviewIds);
            List<InterviewItem> items = interviewItemService.list(itemWrapper);

            // 总答题数
            result.put("totalAnswers", items.size());

            // 已评分题目数
            long scoredCount = items.stream()
                    .filter(i -> i.getAiScore() != null)
                    .count();
            result.put("scoredAnswers", scoredCount);

            // 题目平均分
            double itemAvgScore = items.stream()
                    .filter(i -> i.getAiScore() != null)
                    .mapToDouble(i -> i.getAiScore().doubleValue())
                    .average()
                    .orElse(0.0);
            result.put("itemAverageScore", BigDecimal.valueOf(itemAvgScore).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("totalAnswers", 0);
            result.put("scoredAnswers", 0);
            result.put("itemAverageScore", BigDecimal.ZERO);
        }

        return R.ok(result);
    }
}
