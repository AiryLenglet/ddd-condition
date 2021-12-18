package conditions.iam.repository;

import conditions.context.UserProvider;
import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import conditions.core.model.Country;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.Specification;
import conditions.iam.cross_border.InvestmentCrossBorderRule;
import conditions.iam.model.User;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import static conditions.core.repository.Specification.not;

public class IamConditionRepository implements ConditionRepository {

    private final ConditionRepository conditionRepository;
    private final UserRepository userRepository;
    private final UserProvider userProvider;

    public IamConditionRepository(
            ConditionRepository conditionRepository,
            UserRepository userRepository,
            UserProvider userProvider
    ) {
        this.conditionRepository = conditionRepository;
        this.userRepository = userRepository;
        this.userProvider = userProvider;
    }

    @Override
    public void save(Condition condition) {
        if (condition.getOwner() != null) {
            final var owner = this.userRepository.findById(condition.getOwner().getPid());
            if (!owner.isApprover()) {
                throw new IllegalArgumentException("owner must be approver");
            }
        }
        this.conditionRepository.save(condition);
    }

    @Override
    public Condition findById(ConditionId id) {
        return this.conditionRepository.findById(id);
    }

    @Override
    public Iterable<Condition> findAll(Specification<Condition> specification) {
        final User user = this.userRepository.findById(this.userProvider.currentUser());
        return findAll(specification.and(crossBorder(user.location())));
    }

    private Specification<Condition> crossBorder(Country userLocation) {
        return hasNoBookingLocation()
                .or(investmentCrossBorder(userLocation));
    }

    private Specification<Condition> investmentCrossBorder(Country userLocation) {
        return isInvestment()
                .and(not((root, query, criteriaBuilder) -> bookingLocationPath(root).in(InvestmentCrossBorderRule.canNotSee(userLocation))));
    }

    private Specification<Condition> isInvestment() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isRecurring")); //todo: change impl
    }

    private Specification<Condition> hasNoBookingLocation() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(bookingLocationPath(root));
    }

    private Path<Object> bookingLocationPath(Root<Condition> root) {
        return root.get("bookingLocation.code");
    }
}
