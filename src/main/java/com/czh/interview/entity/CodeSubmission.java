package com.czh.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("code_submission")
public class CodeSubmission {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long interviewItemId;

    private String language;

    private String code;

    private String result;

    private BigDecimal score;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
