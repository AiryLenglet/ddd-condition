package conditions.core.model.task;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.reflect.ParameterizedType;

public abstract class DecisionTask<E extends Enum<E>> extends Task {

    @Enumerated(EnumType.STRING)
    protected E outcome;

    DecisionTask() {
        //package-private for hibernate
    }

    protected DecisionTask(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee
    ) {
        super(
                conditionId,
                fulfillmentId,
                assignee
        );
    }

    protected DecisionTask(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee,
            TaskId previousTaskId
    ) {
        super(
                conditionId,
                fulfillmentId,
                assignee,
                previousTaskId
        );
    }

    public E getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = Enum.valueOf(
                (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0],
                outcome
        );
    }
}
