package com.czh.interview.mapper;

import com.czh.interview.entity.Interview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {
}
