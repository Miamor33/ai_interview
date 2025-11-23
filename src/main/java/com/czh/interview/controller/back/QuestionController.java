package com.czh.interview.controller.back;

import com.czh.interview.entity.Question;
import com.czh.interview.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> getAll() {
        return questionService.list();
    }

    @GetMapping("/{id}")
    public Question getById(@PathVariable Long id) {
        return questionService.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody Question question) {
        return questionService.save(question);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        return questionService.updateById(question);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return questionService.removeById(id);
    }
}
