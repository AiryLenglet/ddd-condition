package conditions.spring;

import conditions.core.model.Condition;
import conditions.core.repository.ConditionRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class DashboardService {

    private final ConditionRepository conditionRepository;

    public DashboardService(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    public Stream<Condition> getUserConditions() {
        return this.conditionRepository.findAll((root, query, criteriaBuilder) -> root.isNotNull());
    }
}
