package com.czh.interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.interview.entity.Interview;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.handler.BizException;
import com.czh.interview.mapper.InterviewItemMapper;
import com.czh.interview.mapper.InterviewMapper;
import com.czh.interview.service.InterviewItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class InterviewItemServiceImpl extends ServiceImpl<InterviewItemMapper, InterviewItem> implements InterviewItemService {

    @Autowired
    private InterviewItemMapper interviewItemMapper;

    @Autowired
    private InterviewMapper interviewMapper;

    @Override
    @Transactional
    public void submitAnswer(Long interviewItemId, String candidateAnswer) {

        // 1. 更新候选人答案
        InterviewItem item = interviewItemMapper.selectById(interviewItemId);
        Interview interview = interviewMapper.selectById(item.getInterviewId());
        // 状态已完成不允许更新
        if (interview != null) {
            if (interview.getStatus().equals(1)){
                throw new BizException("面试已经结束 不能更新答案了");
            }
        }
        item.setCandidateAnswer(candidateAnswer);

        // 2. 调用AI评分（这里模拟一个逻辑：匹配到关键字加分）
        double score = aiEvaluate(item.getCandidateAnswer(), item.getQuestionId());
        item.setAiScore(BigDecimal.valueOf(score));
        interviewItemMapper.updateById(item);

        // 3. 更新面试总分
        List<InterviewItem> items = interviewItemMapper.selectList(
                new QueryWrapper<InterviewItem>().eq("interview_id", item.getInterviewId())
        );
        double avg = items.stream()
                .filter(i -> i.getAiScore() != null)
                .mapToDouble(i -> i.getAiScore().doubleValue())
                .average().orElse(0.0);

        interview.setScore(BigDecimal.valueOf(avg));
        interviewMapper.updateById(interview);
    }

    @Override
    public List<InterviewItem> getByInterviewId(Long interviewId) {
        QueryWrapper<InterviewItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interview_id", interviewId);
        return interviewItemMapper.selectList(queryWrapper);
    }

    private double aiEvaluate(String answer, Long questionId) {
        // 模拟AI评分逻辑
        if (answer == null || answer.isEmpty()) return 0.0;
        if (answer.toLowerCase().contains("java")) return 90.0;
        return 60.0 + new Random().nextInt(20); // 60-80分之间
    }
}
