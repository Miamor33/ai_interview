package com.czh.interview.common.vo;

import lombok.Data;

@Data
public class QuestionVO {
    private Long id;
    private String content;
    private String type;
    private Integer difficulty;
}
