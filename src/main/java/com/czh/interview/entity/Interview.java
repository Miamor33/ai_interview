package com.czh.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("interview")
public class Interview {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long candidateId;

    /**
     * 0=进行中,1=完成
     */
    private Integer status;

    private BigDecimal score;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
