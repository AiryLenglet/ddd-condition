package conditions.iam.repository;

import conditions.context.UserProvider;
import conditions.core.model.Condition;
import conditions.core.model.Country;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.Specification;
import conditions.iam.cross_border.AssetManagementCrossBorderRule;
import conditions.iam.cross_border.InvestmentCrossBorderRule;
import conditions.iam.model.User;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        final User user = this.userRepository.findById(this.userProvider.currentUser());

        if (!this.canSeeCondition(user, condition)) {
            throw new EntityNotFoundException();
        }

        if (condition.getOwner() != null) {
            final var owner = this.userRepository.findById(condition.getOwner().getPid());
            if (!owner.isApprover()) {
                throw new IllegalArgumentException("owner must be approver");
            }
        }
        this.conditionRepository.save(condition);
    }

    @Override
    public Condition findOne(Specification<Condition> specification) {
        final User user = this.userRepository.findById(this.userProvider.currentUser());
        return this.conditionRepository.findOne(specification.and(crossBorder(user.location())));
    }

    @Override
    public Stream<Condition> findAll(Specification<Condition> specification) {
        final User user = this.userRepository.findById(this.userProvider.currentUser());
        return this.conditionRepository.findAll(specification.and(crossBorder(user.location())));
    }

    private Specification<Condition> crossBorder(Country userLocation) {
        return hasNoBookingLocation()
                .or(investmentCrossBorder(userLocation))
                .or(assetManagementCrossBorder(userLocation));
    }

    private Specification<Condition> investmentCrossBorder(Country userLocation) {
        return isInvestment()
                .and(not((root, query, criteriaBuilder) -> bookingLocationPath(root).in(InvestmentCrossBorderRule.canNotSee(userLocation))));
    }

    private Specification<Condition> assetManagementCrossBorder(Country userLocation) {
        return isAssetManagement()
                .and((root, query, criteriaBuilder) -> bookingLocationPath(root).in(AssetManagementCrossBorderRule.canSee(userLocation)));
    }

    private Specification<Condition> isInvestment() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isRecurring")); //todo: change impl
    }

    private Specification<Condition> isAssetManagement() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isRecurring")); //todo: change impl
    }

    private Specification<Condition> hasNoBookingLocation() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(bookingLocationPath(root));
    }

    private Path<Object> bookingLocationPath(Root<Condition> root) {
        return root.get("bookingLocation.code");
    }

    private boolean canSeeCondition(User aUser, Condition aCondition) {
        if (aCondition.getBookingLocation() != null) {
            //todo: change impl
            final var forbiddenCountries = InvestmentCrossBorderRule.canNotSee(aUser.location());
            if (StreamSupport.stream(forbiddenCountries.spliterator(), false).anyMatch(c -> aCondition.getBookingLocation().equals(c))) {
                return false;
            }
            final var allowedCountries = AssetManagementCrossBorderRule.canSee(aUser.location());
            if (StreamSupport.stream(allowedCountries.spliterator(), false).noneMatch(c -> aCondition.getBookingLocation().equals(c))) {
                return false;
            }
        }
        return true;
    }
}
