package com.czh.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.interview.entity.CodeSubmission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CodeSubmissionMapper extends BaseMapper<CodeSubmission> {
}
