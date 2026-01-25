package com.czh.interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.interview.common.dto.AiEvaluationResult;
import com.czh.interview.entity.Interview;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.entity.Question;
import com.czh.interview.handler.BizException;
import com.czh.interview.mapper.InterviewItemMapper;
import com.czh.interview.mapper.InterviewMapper;
import com.czh.interview.service.AiEvaluationService;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewItemServiceImpl extends ServiceImpl<InterviewItemMapper, InterviewItem> implements InterviewItemService {

    private final InterviewItemMapper interviewItemMapper;
    private final InterviewMapper interviewMapper;
    private final AiEvaluationService aiEvaluationService;
    private final QuestionService questionService;

    @Override
    @Transactional
    public void submitAnswer(Long interviewItemId, String candidateAnswer) {

        // 1. 更新候选人答案
        InterviewItem item = interviewItemMapper.selectById(interviewItemId);
        if (item == null) {
            throw new BizException("面试题目不存在");
        }
        
        Interview interview = interviewMapper.selectById(item.getInterviewId());
        // 状态已完成不允许更新
        if (interview != null) {
            if (interview.getStatus().equals(1)){
                throw new BizException("面试已经结束 不能更新答案了");
            }
        }
        item.setCandidateAnswer(candidateAnswer);

        // 2. 调用AI评分
        Question question = questionService.getById(item.getQuestionId());
        if (question == null) {
            throw new BizException("题目不存在");
        }
        
        AiEvaluationResult evaluationResult = aiEvaluationService.evaluate(question, candidateAnswer);
        item.setAiScore(evaluationResult.getTotalScore());
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
}
