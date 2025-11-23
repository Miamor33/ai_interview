package com.czh.interview.common.vo;

import lombok.Data;

@Data
public class InterviewItemVO {
    private Long id;
    private Long interviewId;
    private QuestionVO question;
    private String answer;
    private Double aiScore;
}
