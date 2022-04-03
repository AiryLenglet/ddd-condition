package conditions.core.event.approval;

import conditions.core.event.TaskEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.TaskId;

public record ApprovalRejectedEvent(
        ConditionId conditionId,
        FulfillmentId fulfillmentId,
        TaskId taskId
) implements TaskEvent {
}
