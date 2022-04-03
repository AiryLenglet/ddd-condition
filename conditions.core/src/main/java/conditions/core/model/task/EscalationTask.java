package conditions.core.model.task;

import conditions.common.util.Validate;
import conditions.core.event.approval.EscalationDiscardedEvent;
import conditions.core.event.approval.EscalationResolvedEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ESCALATION")
public class EscalationTask extends DecisionTask<EscalationTask.Decision> {

    public EscalationTask(
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

    EscalationTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        Validate.notNull(this.comment, () -> new IllegalStateException("provide comment"));

        this.addEvent(switch (this.outcome) {
            case RESOLVE -> new EscalationResolvedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case DISCARD -> new EscalationDiscardedEvent(this.conditionId, this.fulfillmentId, this.taskId);
        });

        super.submit();
    }

    public enum Decision {
        RESOLVE,
        DISCARD
    }
}
