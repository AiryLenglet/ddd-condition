package conditions.iam.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;

public class IamConditionRepository implements ConditionRepository {

    private final ConditionRepository conditionRepository;
    private final UserRepository userRepository;

    public IamConditionRepository(
            ConditionRepository conditionRepository,
            UserRepository userRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(Condition condition) {
        final var user = this.userRepository.findById(condition.getOwner().getPid());
        if (!user.isApprover()) {
            throw new IllegalArgumentException("owner must be approver");
        }
        this.conditionRepository.save(condition);
    }

    @Override
    public Condition findById(ConditionId id) {
        return this.conditionRepository.findById(id);
    }
}
