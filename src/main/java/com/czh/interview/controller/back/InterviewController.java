package com.czh.interview.controller.back;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.interview.common.R;
import com.czh.interview.entity.Interview;
import com.czh.interview.entity.InterviewItem;
import com.czh.interview.entity.Question;
import com.czh.interview.service.InterviewItemService;
import com.czh.interview.service.InterviewService;
import com.czh.interview.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/interviews")
@RestController
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    public List<Interview> getAll() {
        return interviewService.list();
    }

    @GetMapping("/{id}")
    public Interview getById(@PathVariable Long id) {
        return interviewService.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody Interview interview) {
        return interviewService.save(interview);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Interview interview) {
        interview.setId(id);
        return interviewService.updateById(interview);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return interviewService.removeById(id);
    }
}
