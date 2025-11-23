package com.czh.interview.controller.back;

import com.czh.interview.common.vo.InterviewItemVO;
import com.czh.interview.common.vo.QuestionVO;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.entity.Question;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.common.R;
import com.czh.interview.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interview-items")
public class InterviewItemController {

    @Autowired
    private InterviewItemService interviewItemService;
    @Autowired
    private QuestionService questionService;

    /**
     * 批量
     * @param interviewId
     * @return
     */
    @GetMapping
    public R<List<InterviewItemVO>> getAll(@RequestParam Long interviewId) {
        List<InterviewItem> items = interviewItemService.getByInterviewId(interviewId);

        // 假设 interviewItemService 可以提供批量查询题目信息的方法
        List<Long> questionIds = items.stream()
                .map(InterviewItem::getQuestionId)
                .collect(Collectors.toList());
        Map<Long, Question> questionMap = questionService.getByIds(questionIds)
                .stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<InterviewItemVO> vos = items.stream().map(item -> {
            InterviewItemVO vo = new InterviewItemVO();
            vo.setId(item.getId());
            vo.setInterviewId(item.getInterviewId());
            vo.setAnswer(item.getCandidateAnswer());
            if(item.getAiScore()!=null) {
                vo.setAiScore(item.getAiScore().doubleValue());
            }
            Question question = questionMap.get(item.getQuestionId());
            if (question != null) {
                QuestionVO questionVO = new QuestionVO();
                questionVO.setId(question.getId());
                questionVO.setContent(question.getContent());
                questionVO.setType(question.getType());
                questionVO.setDifficulty(question.getDifficulty());
                vo.setQuestion(questionVO);
            }

            return vo;
        }).collect(Collectors.toList());

        return R.ok(vos);
    }

    /**
     * 单个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<InterviewItemVO> getById(@PathVariable Long id) {
        // 1. 查询面试题目
        InterviewItem item = interviewItemService.getById(id);
        if (item == null) {
            return R.error("未找到对应面试题目");
        }

        // 2. 查询题目信息
        Question question = questionService.getById(item.getQuestionId());

        // 3. 构建 VO
        InterviewItemVO vo = new InterviewItemVO();
        vo.setId(item.getId());
        vo.setInterviewId(item.getInterviewId());
        vo.setAnswer(item.getCandidateAnswer());
        if(item.getAiScore() != null) {
            vo.setAiScore(item.getAiScore().doubleValue());
        }

        if (question != null) {
            QuestionVO questionVO = new QuestionVO();
            questionVO.setId(question.getId());
            questionVO.setContent(question.getContent());
            questionVO.setType(question.getType());
            questionVO.setDifficulty(question.getDifficulty());
            vo.setQuestion(questionVO);
        }

        return R.ok(vo);
    }



}
