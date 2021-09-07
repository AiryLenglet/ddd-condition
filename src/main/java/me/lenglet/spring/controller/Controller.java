package me.lenglet.spring.controller;

import me.lenglet.core.use_case.CreateConditionUseCase;
import me.lenglet.core.use_case.OpenConditionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    private CreateConditionUseCase createConditionUseCase;
    @Autowired
    private OpenConditionUseCase openConditionUseCase;

    @GetMapping
    void test() {
        int i = 0;
        i++;
    }

    @PostMapping
    public CreateConditionUseCase.Response create() {
        return this.createConditionUseCase.execute();
    }

    @PutMapping(path = "/{id}")
    public void open(@PathVariable("id") String id) {
        this.openConditionUseCase.execute(OpenConditionUseCase.Request.builder().conditionId(id).build());
    }
}
