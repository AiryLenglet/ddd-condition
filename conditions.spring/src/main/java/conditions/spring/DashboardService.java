package conditions.spring;

import conditions.core.repository.ConditionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ConditionRepository conditionRepository;

    public DashboardService(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    public Iterable getUserConditions() {
        return this.conditionRepository.findAll((root, query, criteriaBuilder) -> root.isNotNull());
    }
}
