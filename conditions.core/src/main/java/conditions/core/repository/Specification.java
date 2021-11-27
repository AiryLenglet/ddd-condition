package conditions.core.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public interface Specification<T> {

    static <T> Specification<T> not(Specification<T> spec) {
        return spec == null
                ? (root, query, builder) -> null
                : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
    }

    static <T> Specification<T> where(Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : spec;
    }

    default Specification<T> and(Specification<T> other) {
        return composed(this, other, CriteriaBuilder::and);
    }

    default Specification<T> or(Specification<T> other) {
        return composed(this, other, CriteriaBuilder::or);
    }

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

    static <T> Specification<T> composed(
            Specification<T> lhs,
            Specification<T> rhs,
            Combiner combiner
    ) {
        return (root, query, builder) -> {

            final var thisPredicate = toPredicate(lhs, root, query, builder);
            final var otherPredicate = toPredicate(rhs, root, query, builder);

            if (thisPredicate == null) {
                return otherPredicate;
            }

            return otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate);
        };
    }

    private static <T> Predicate toPredicate(
            Specification<T> specification,
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder builder
    ) {
        return specification == null ? null : specification.toPredicate(root, query, builder);
    }

    interface Combiner extends Serializable {
        Predicate combine(CriteriaBuilder builder, Predicate lhs, Predicate rhs);
    }
}
