package me.lenglet.core.repository;

import me.lenglet.core.model.Condition;
import me.lenglet.core.model.ConditionId;

public interface ConditionRepository {
    Condition save(Condition condition);

    Condition findById(ConditionId id);
}
