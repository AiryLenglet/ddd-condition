package conditions.core.model;

public record ConditionStatusProjection(
        ConditionId conditionId,
        Condition.Status status
) {
}
