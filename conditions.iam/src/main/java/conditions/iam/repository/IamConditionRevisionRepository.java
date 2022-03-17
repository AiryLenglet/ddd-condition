package conditions.iam.repository;

import conditions.context.UserProvider;
import conditions.core.model.Condition;
import conditions.core.model.ConditionRevision;
import conditions.core.repository.ConditionRevisionRepository;
import conditions.core.repository.Specification;

import javax.persistence.criteria.CriteriaQuery;
import java.util.stream.Stream;

import static conditions.iam.repository.utils.ExpressionFactory.isInvolvedInCondition;

public class IamConditionRevisionRepository implements ConditionRevisionRepository {

    private final ConditionRevisionRepository delegate;
    private final UserProvider userProvider;

    public IamConditionRevisionRepository(
            ConditionRevisionRepository delegate,
            UserProvider userProvider
    ) {
        this.delegate = delegate;
        this.userProvider = userProvider;
    }

    @Override
    public void save(ConditionRevision condition) {
        this.delegate.save(condition);
    }

    @Override
    public Stream<ConditionRevision> findAll(Specification<ConditionRevision> specification) {
        return this.delegate.findAll(specification.and(canSeeCondition()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Specification<ConditionRevision> canSeeCondition() {
        return (root, query, criteriaBuilder) -> {

            ((CriteriaQuery) query).select(query.from(ConditionRevision.class)).distinct(true);
            final var conditionRoot = query.from(Condition.class);
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("conditionId"), conditionRoot.get("conditionId")),
                    isInvolvedInCondition(criteriaBuilder, conditionRoot, userProvider.currentUser())
            );
        };
    }
}
