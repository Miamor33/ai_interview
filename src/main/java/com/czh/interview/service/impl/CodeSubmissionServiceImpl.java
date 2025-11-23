package com.czh.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.interview.entity.CodeSubmission;
import com.czh.interview.mapper.CodeSubmissionMapper;
import com.czh.interview.service.CodeSubmissionService;
import org.springframework.stereotype.Service;

@Service
public class CodeSubmissionServiceImpl extends ServiceImpl<CodeSubmissionMapper, CodeSubmission> implements CodeSubmissionService {
}
