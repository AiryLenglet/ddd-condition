package conditions.micronaut;

import conditions.core.model.*;
import conditions.core.model.task.*;
import conditions.core.model.ConditionStatusProjection;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;

@Introspected(classes = {
        ApprovalTask.class,
        ChangeReviewTask.class,
        ConditionSetupTask.class,
        EscalationTask.class,
        FulfillmentTask.class,
        RefusalReviewTask.class,
        ReviewTask.class,
        VerificationTask.class,

        Condition.class,
        ConditionRevision.class,
        Metadata.class,
        Fulfillment.class,
        TaskRevision.class
})

@TypeHint(
        typeNames = {
          "conditions.core.model.Metadata$Data",
          "conditions.core.model.Metadata$Type"
        },
        value = {
                Condition.class,
                ConditionId.class,
                Condition.Status.class,
                Condition.Classification.class,
                ConditionRevision.class,
                ConditionRevisionId.class,
                Metadata.class,

                Fulfillment.class,
                FulfillmentId.class,
                Fulfillment.Status.class,
                Fulfillment.Type.class,

                Task.class,
                TaskId.class,
                Task.Status.class,
                TaskRevision.class,
                TaskRevisionId.class,

                ApprovalTask.class,
                ChangeReviewTask.class,
                ConditionSetupTask.class,
                EscalationTask.class,
                FulfillmentTask.class,
                RefusalReviewTask.class,
                ReviewTask.class,
                VerificationTask.class,

                Pid.class,
                UuidId.class,
                Country.class,

                conditions.api.model.ConditionId.class,
                conditions.api.model.Condition.class,
                conditions.api.model.CompleteTask.class,
                conditions.api.model.Task.class,

                EncryptionConverter.class,

                ConditionStatusProjection.class
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
