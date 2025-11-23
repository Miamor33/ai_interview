package com.czh.interview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.interview.entity.InterviewItem;

import java.util.List;

public interface InterviewItemService extends IService<InterviewItem> {

    void submitAnswer(Long interviewItemId, String candidateAnswer);

    List<InterviewItem> getByInterviewId(Long interviewId);
}
