package me.lenglet.spring.repository;

import me.lenglet.core.model.Condition;
import me.lenglet.core.model.ConditionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringConditionRepository extends JpaRepository<Condition, ConditionId> {
}
