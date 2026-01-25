package com.czh.interview.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.interview.common.R;
import com.czh.interview.entity.Interview;
import com.czh.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 面试历史控制器
 */
@RestController
@RequestMapping("/api/interview/history")
@RequiredArgsConstructor
public class InterviewHistoryController {

    private final InterviewService interviewService;

    /**
     * 获取面试历史列表（分页）
     */
    @GetMapping("/list")
    public R<Page<Interview>> list(
            @RequestParam(required = false) Long candidateId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Interview> page = new Page<>(current, size);
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        
        if (candidateId != null) {
            wrapper.eq(Interview::getCandidateId, candidateId);
        }
        if (status != null) {
            wrapper.eq(Interview::getStatus, status);
        }
        if (startTime != null) {
            wrapper.ge(Interview::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(Interview::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Interview::getCreateTime);
        
        Page<Interview> result = interviewService.page(page, wrapper);
        return R.ok(result);
    }

    /**
     * 获取面试详情
     */
    @GetMapping("/{id}")
    public R<Interview> getDetail(@PathVariable Long id) {
        Interview interview = interviewService.getById(id);
        if (interview == null) {
            return R.error("面试记录不存在");
        }
        return R.ok(interview);
    }
}
