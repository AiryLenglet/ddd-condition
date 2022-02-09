package conditions.core.repository;

import conditions.core.repository.Specification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

public class EntityManagerRepository<T> {

    private final EntityManager entityManager;
    private final Class<T> type;

    public EntityManagerRepository(
            EntityManager entityManager,
            Class<T> type
    ) {
        this.entityManager = entityManager;
        this.type = type;
    }

    public void save(T entity) {
        if (this.entityManager.contains(entity)) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }
    }

    public Stream<T> findAll(Specification<T> specification) {
        return this.specificationQuery(specification).getResultList().stream();
    }

    public T findOne(Specification<T> specification) {
        return this.specificationQuery(specification).getSingleResult();
    }

    protected TypedQuery<T> specificationQuery(Specification<T> specification) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = criteriaBuilder.createQuery(this.type);
        final Root<T> root = query.from(this.type);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        return this.entityManager.createQuery(query);
    }
}
