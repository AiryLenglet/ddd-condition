package conditions.spring.controller;

import conditions.core.use_case.ApproveConditionUseCase;
import conditions.core.use_case.CreateConditionUseCase;
import conditions.core.use_case.Request;
import conditions.core.use_case.SubmitConditionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    private CreateConditionUseCase createConditionUseCase;
    @Autowired
    private SubmitConditionUseCase submitConditionUseCase;
    @Autowired
    private ApproveConditionUseCase approveConditionUseCase;

    @GetMapping
    void test() {
        int i = 0;
        i++;
    }

    @PostMapping
    public CreateConditionUseCase.Response create() {
        return this.createConditionUseCase.execute();
    }

    @PostMapping(path = "/{id}/submit")
    public void submit(
            @PathVariable("id") String id
    ) {
        this.submitConditionUseCase.execute(new Request(id));
    }

    @PostMapping(path = "/{id}/approve")
    public void approve(
            @PathVariable("id") String id
    ) {
        this.approveConditionUseCase.execute(id);
    }

    @PutMapping(path = "/{id}")
    public void open(@PathVariable("id") String id) {
        this.submitConditionUseCase.execute(new Request(id));
    }
}
