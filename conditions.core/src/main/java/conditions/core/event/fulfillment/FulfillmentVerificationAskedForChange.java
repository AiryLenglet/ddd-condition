package conditions.core.event.fulfillment;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.TaskId;

public record FulfillmentVerificationAskedForChange(
        ConditionId conditionId,
        TaskId taskId
) implements Event {
}
