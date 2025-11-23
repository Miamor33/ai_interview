package com.czh.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.interview.entity.Interview;
import com.czh.interview.mapper.InterviewMapper;
import com.czh.interview.service.InterviewService;
import org.springframework.stereotype.Service;

@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements InterviewService {
    // 自定义方法可在这里实现
}
