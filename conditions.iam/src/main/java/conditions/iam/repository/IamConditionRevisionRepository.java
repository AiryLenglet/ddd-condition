package conditions.iam.repository;

import conditions.context.UserProvider;
import conditions.core.model.ConditionRevision;
import conditions.core.repository.ConditionRevisionRepository;
import conditions.core.repository.Specification;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.stream.Stream;

public class IamConditionRevisionRepository implements ConditionRevisionRepository {

    private final ConditionRevisionRepository delegate;
    private final UserProvider userProvider;
    private final EntityManager entityManager;

    public IamConditionRevisionRepository(
            ConditionRevisionRepository delegate,
            UserProvider userProvider,
            EntityManager entityManager) {
        this.delegate = delegate;
        this.userProvider = userProvider;
        this.entityManager = entityManager;
    }

    @Override
    public void persist(ConditionRevision condition) {
        this.delegate.persist(condition);
    }

    @Override
    public Stream<ConditionRevision> findAll(Specification<ConditionRevision> specification) {
        entityManager.unwrap(Session.class)
                .enableFilter("involvedInCondition")
                .setParameter("user", this.userProvider.currentUser().getValue())
                .validate();
        return this.delegate.findAll(specification);
    }
}
