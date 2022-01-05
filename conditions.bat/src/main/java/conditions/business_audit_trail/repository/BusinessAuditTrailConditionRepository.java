package conditions.business_audit_trail.repository;

import conditions.context.UserProvider;
import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessAuditTrailConditionRepository implements ConditionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAuditTrailConditionRepository.class);

    private final ConditionRepository delegate;
    private final BusinessAuditTrail businessAuditTrail;
    private final UserProvider userProvider;

    public BusinessAuditTrailConditionRepository(
            ConditionRepository delegate,
            BusinessAuditTrail businessAuditTrail,
            UserProvider userProvider
    ) {
        this.delegate = delegate;
        this.businessAuditTrail = businessAuditTrail;
        this.userProvider = userProvider;
    }

    @Override
    public void save(Condition condition) {
        LOGGER.info("save {}", condition);
        this.delegate.save(condition);
        this.businessAuditTrail.save("user " + this.userProvider.currentUser() + " updated condition " + condition.getConditionId().getId());
    }

    @Override
    public Condition findById(ConditionId id) {
        LOGGER.info("findById {}", id);
        final var condition = this.delegate.findById(id);
        this.businessAuditTrail.save("user " + this.userProvider.currentUser() + " accessed condition " + id);
        return condition;
    }

    @Override
    public Iterable<Condition> findAll(Specification<Condition> specification) {
        LOGGER.info("findAll with specifications {}", specification);
        return this.delegate.findAll(specification);
    }

    public interface BusinessAuditTrail {
        void save(String businessAuditTrailMessage);
    }
}