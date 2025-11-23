package com.czh.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.interview.entity.InterviewItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface InterviewItemMapper extends BaseMapper<InterviewItem> {
}
