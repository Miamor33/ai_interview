package com.czh.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("interview_item")
public class InterviewItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long interviewId;

    private Long questionId;

    private String candidateAnswer;

    private BigDecimal aiScore;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
