package conditions.core.event;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.TaskId;

public interface ConditionEvent extends Event {

    ConditionId conditionId();
}
