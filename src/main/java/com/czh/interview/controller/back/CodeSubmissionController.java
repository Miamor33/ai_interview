package com.czh.interview.controller.back;

import com.czh.interview.entity.CodeSubmission;
import com.czh.interview.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/code-submissions")
public class CodeSubmissionController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @GetMapping
    public List<CodeSubmission> getAll() {
        return codeSubmissionService.list();
    }

    @GetMapping("/{id}")
    public CodeSubmission getById(@PathVariable Long id) {
        return codeSubmissionService.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody CodeSubmission submission) {
        return codeSubmissionService.save(submission);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody CodeSubmission submission) {
        submission.setId(id);
        return codeSubmissionService.updateById(submission);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return codeSubmissionService.removeById(id);
    }
}
