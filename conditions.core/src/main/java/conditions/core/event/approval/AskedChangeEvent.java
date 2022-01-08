package conditions.core.event.approval;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.TaskId;

public record AskedChangeEvent(
        ConditionId conditionId,
        FulfillmentId fulfillmentId,
        TaskId taskId
) implements Event {
}
