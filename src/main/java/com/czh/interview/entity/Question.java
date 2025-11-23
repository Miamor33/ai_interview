package com.czh.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * text, coding, video
     */
    private String type;

    private String content;

    private String answer;

    private Integer difficulty;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
