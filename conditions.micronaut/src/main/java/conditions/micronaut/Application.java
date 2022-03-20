package conditions.micronaut;

import conditions.core.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;

@Introspected(classes = {
        FulfillmentTask.class,
        Condition.class,
        ConditionRevision.class,
        Fulfillment.class,
        TaskRevision.class
})

@TypeHint(
        value = {
                Condition.class,
                ConditionId.class,
                Condition.Status.class,
                ConditionRevision.class,
                ConditionRevisionId.class,

                Fulfillment.class,
                FulfillmentId.class,
                Fulfillment.Status.class,

                Task.class,
                TaskId.class,
                Task.Status.class,
                TaskRevision.class,
                TaskRevisionId.class,

                FulfillmentTask.class,

                Pid.class,
                UuidId.class,
                Country.class,
                Imposer.class,
                Owner.class,

                conditions.api.model.ConditionId.class,
                conditions.api.model.Condition.class,
                conditions.api.model.CompleteTask.class,
                conditions.api.model.Task.class
        },
        accessType = {
                TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS,
                TypeHint.AccessType.ALL_DECLARED_FIELDS,
                TypeHint.AccessType.ALL_DECLARED_METHODS
        }
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
