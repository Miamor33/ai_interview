package com.czh.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.interview.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
