package com.czh.interview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.interview.entity.Question;

import java.util.Arrays;
import java.util.List;

public interface QuestionService extends IService<Question> {
    List<Question> getByIds(List<Long> questionIds);

}
