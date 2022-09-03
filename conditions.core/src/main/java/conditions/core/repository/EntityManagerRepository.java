package conditions.core.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.util.Arrays;
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

    public void persist(T entity) {
        this.entityManager.persist(entity);
    }

    public Stream<T> findAll(Specification<T> specification) {
        return this.specificationQuery(specification).getResultList().stream();
    }

    public <P> Stream<P> findAll(Specification<T> specification, Class<P> projection) {
        return this.specificationQuery(specification, projection).getResultList().stream();
    }

    public <P> P findOne(Specification<T> specification, Class<P> projection) {
        return this.specificationQuery(specification, projection).getSingleResult();
    }

    public T findOne(Specification<T> specification) {
        return this.specificationQuery(specification).getSingleResult();
    }

    protected TypedQuery<T> specificationQuery(Specification<T> specification) {
        return this.specificationQuery(specification, this.type);
    }

    protected <P> TypedQuery<P> specificationQuery(Specification<T> specification, Class<P> projectionClass) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<P> query = criteriaBuilder.createQuery(projectionClass);
        final Root<T> root = query.from(this.type);

        if (!this.type.equals(projectionClass)) {
            query.select(criteriaBuilder.construct(projectionClass, selections(projectionClass, root)));
        }

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        return this.entityManager.createQuery(query);
    }

    protected Selection<?>[] selections(Class<?> projectionClass, Root<?> root) {
        return Arrays.stream(projectionClass.getDeclaredFields())
                .map(Field::getName)
                .map(root::get)
                .toArray(Selection<?>[]::new);
    }
}
