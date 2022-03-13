package conditions.micronaut;

import conditions.core.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;

@Introspected(classes = {
        FulfillmentTask.class,
        Condition.class
})

@TypeHint(
        value = {
                Condition.class,
                ConditionId.class,
                Condition.Status.class,

                Fulfillment.class,
                FulfillmentId.class,

                Task.class,
                TaskId.class,
                Task.Status.class,

                FulfillmentTask.class,

                Pid.class,
                UuidId.class,
                Country.class,
                Imposer.class,
                Owner.class
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
