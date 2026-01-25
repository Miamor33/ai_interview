package com.czh.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 面试反馈表
 */
@Data
@TableName("interview_feedback")
public class InterviewFeedback {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long interviewItemId;

    /**
     * 反馈类型：real_time/final
     */
    private String feedbackType;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 识别的关键词（JSON格式）
     */
    private String keywords;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
