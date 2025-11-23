package com.czh.interview.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.interview.common.R;
import com.czh.interview.common.dto.AnswerRequest;
import com.czh.interview.entity.Interview;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.entity.Question;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.service.InterviewService;
import com.czh.interview.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/interview-flow")
@RequiredArgsConstructor
public class InterviewFlowController {

    private final InterviewService interviewService;
    private final QuestionService questionService;
    private final InterviewItemService interviewItemService;

    /**
     * 1️⃣ 开始面试
     */
    @GetMapping("/start")
    public R<Interview> start(@RequestParam Long candidateId) {
        // 新建面试记录
        Interview interview = new Interview();
        interview.setCandidateId(candidateId);
        interview.setStatus(0);
        interviewService.save(interview);

        // 随机抽题（示例 3 道）
        List<Question> questions = questionService.lambdaQuery().last("limit 3").list();

        // 保存面试题记录
        for (Question q : questions) {
            InterviewItem item = new InterviewItem();
            item.setInterviewId(interview.getId());
            item.setQuestionId(q.getId());
            interviewItemService.save(item);
        }

        return R.ok(interview);
    }

    /**
     * 2️⃣ 提交答案
     */
    @PostMapping("/answer")
    public R<String> answer(@RequestBody AnswerRequest request) {
        interviewItemService.submitAnswer(request.getInterviewItemId(), request.getCandidateAnswer());
        return R.ok("答案提交并评分完成");
    }

    /**
     * 3️⃣ 完成面试
     */
    @GetMapping("/finish")
    public R<Interview> finish(@RequestParam Long interviewId) {
        Interview interview = interviewService.getById(interviewId);
        if (interview == null) {
            return R.error("面试不存在");
        }

        // 获取所有题目评分
        List<InterviewItem> items = interviewItemService.list(
                new LambdaQueryWrapper<InterviewItem>().eq(InterviewItem::getInterviewId, interviewId)
        );

        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (InterviewItem item : items) {
            if (item.getAiScore() != null) {
                total = total.add(item.getAiScore());
                count++;
            }
        }

        BigDecimal avg = count > 0
                ? total.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        // 更新面试状态 & 分数
        interview.setStatus(1);
        interview.setScore(avg);
        interviewService.updateById(interview);

        return R.ok(interview);
    }
}
