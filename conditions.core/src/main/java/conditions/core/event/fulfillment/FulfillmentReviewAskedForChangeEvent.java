package conditions.core.event.fulfillment;

import conditions.core.event.TaskEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.TaskId;

public record FulfillmentReviewAskedForChangeEvent(
        ConditionId conditionId,
        FulfillmentId fulfillmentId,
        TaskId taskId
) implements TaskEvent {
}
