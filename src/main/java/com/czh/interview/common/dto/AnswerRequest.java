package com.czh.interview.common.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private Long interviewItemId;
    private String candidateAnswer;
}
