package conditions.iam.repository;

import conditions.context.UserProvider;
import conditions.core.model.Condition;
import conditions.core.model.Country;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.Specification;
import conditions.iam.cross_border.InvestmentCrossBorderRule;
import conditions.iam.model.User;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IamConditionRepository implements ConditionRepository {

    private final ConditionRepository conditionRepository;
    private final UserRepository userRepository;
    private final UserProvider userProvider;
    private final EntityManager entityManager;

    public IamConditionRepository(
            ConditionRepository conditionRepository,
            UserRepository userRepository,
            UserProvider userProvider,
            EntityManager entityManager
    ) {
        this.conditionRepository = conditionRepository;
        this.userRepository = userRepository;
        this.userProvider = userProvider;
        this.entityManager = entityManager;
    }

    @Override
    public void persist(Condition condition) {

        if (condition.getOwner() != null) {
            final var owner = this.userRepository.findById(condition.getOwner());
            if (!owner.isApprover()) {
                throw new IllegalArgumentException("owner must be approver");
            }
        }
        this.conditionRepository.persist(condition);
    }

    @Override
    public Condition findOne(Specification<Condition> specification) {
        enableCrossBorderRule();
        return this.conditionRepository.findOne(specification);
    }

    @Override
    public <T> T findOne(Specification<Condition> specification, Class<T> projection) {
        enableCrossBorderRule();
        return this.conditionRepository.findOne(specification, projection);
    }

    @Override
    public Stream<Condition> findAll(Specification<Condition> specification) {
        enableCrossBorderRule();
        return this.conditionRepository.findAll(specification);
    }

    private void enableCrossBorderRule() {
        final User user = this.userRepository.findById(this.userProvider.currentUser());
        final var countryBlacklist = StreamSupport.stream(InvestmentCrossBorderRule.canNotSee(user.location()).spliterator(), false)
                .map(Country::getCode)
                .collect(Collectors.toSet());
        this.entityManager.unwrap(Session.class)
                .enableFilter("crossBorder")
                .setParameterList("ASSET_LOCATION_BLACKLIST", countryBlacklist)
                .setParameterList("INVESTMENT_LOCATION_BLACKLIST", countryBlacklist)
                .validate();
    }
}
