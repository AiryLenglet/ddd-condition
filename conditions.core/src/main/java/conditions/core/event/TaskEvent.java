package conditions.core.event;

import conditions.core.model.TaskId;

public interface TaskEvent extends FulfillmentEvent {

    TaskId taskId();
}
