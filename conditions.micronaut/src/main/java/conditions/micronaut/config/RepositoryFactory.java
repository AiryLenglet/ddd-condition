package conditions.micronaut.config;

import conditions.context.UserProvider;
import conditions.core.event.EventBus;
import conditions.core.factory.Clock;
import conditions.core.model.*;
import conditions.core.model.task.Task;
import conditions.core.repository.*;
import conditions.iam.model.User;
import conditions.iam.repository.IamConditionRepository;
import conditions.iam.repository.IamConditionRevisionRepository;
import conditions.iam.repository.UserRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.data.event.listeners.PostPersistEventListener;
import io.micronaut.data.event.listeners.PostUpdateEventListener;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.stream.Stream;

@Factory
public class RepositoryFactory {

    @Bean
    public TaskRepository taskRepository(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        return new TaskRepositoryImpl(eventBus, entityManager);
    }

    @Bean
    public ConditionRepository conditionRepository(
            EventBus eventBus,
            EntityManager entityManager,
            UserRepository userRepository,
            UserProvider userProvider
    ) {
        return new IamConditionRepository(
                new ConditionRepositoryImpl(eventBus, entityManager),
                userRepository,
                userProvider,
                entityManager);
    }

    @Bean
    @Named("newTransactionRequiredConditionRevisionRepository")
    public static class NewTransactionRequiredConditionRevisionRepository implements ConditionRevisionRepository {

        private final ConditionRevisionRepository delegate;

        public NewTransactionRequiredConditionRevisionRepository(ConditionRevisionRepository delegate) {
            this.delegate = delegate;
        }

        @Transactional(value = Transactional.TxType.REQUIRES_NEW)
        @Override
        public void save(ConditionRevision condition) {
            this.delegate.save(condition);
        }

        @Override
        public Stream<ConditionRevision> findAll(Specification<ConditionRevision> specification) {
            return this.delegate.findAll(specification);
        }
    }

    @Bean
    @Named("newTransactionRequiredRevisionTaskRepository")
    public static class NewTransactionRequiredTaskRevisionRepository implements TaskRevisionRepository {

        private final TaskRevisionRepository delegate;

        public NewTransactionRequiredTaskRevisionRepository(TaskRevisionRepository delegate) {
            this.delegate = delegate;
        }

        @Transactional(value = Transactional.TxType.REQUIRES_NEW)
        @Override
        public void save(TaskRevision taskRevision) {
            this.delegate.save(taskRevision);
        }

        @Override
        public Stream<TaskRevision> findAll(Specification<TaskRevision> specification) {
            return this.delegate.findAll(specification);
        }
    }

    @Singleton
    public ConditionListener conditionHistory(
            @Named("newTransactionRequiredConditionRevisionRepository") ConditionRevisionRepository conditionRevisionRepository,
            Clock clock
    ) {
        return new ConditionListener(conditionRevisionRepository, clock);
    }

    @Singleton
    public PostPersistEventListener<Condition> conditionPostPersistEventListener(
            ConditionListener conditionListener
    ) {
        return conditionListener::postPersist;
    }

    @Singleton
    public PostUpdateEventListener<Condition> conditionPostUpdateEventListener(
            ConditionListener conditionListener
    ) {
        return conditionListener::postPersist;
    }

    @Singleton
    public TaskListener taskListener(
            @Named("newTransactionRequiredRevisionTaskRepository") TaskRevisionRepository taskRevisionRepository,
            Clock clock
    ) {
        return new TaskListener(taskRevisionRepository, clock);
    }

    @Singleton
    public PostPersistEventListener<Task> taskPostPersistEventListener(
            TaskListener taskListener
    ) {
        return taskListener::postPersist;
    }

    @Singleton
    public PostUpdateEventListener<Task> taskPostUpdateEventListener(
            TaskListener taskListener
    ) {
        return taskListener::postPersist;
    }

    @Bean
    public FulfillmentRepository fulfillmentRepository(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        return new FulfillmentRepositoryImpl(eventBus, entityManager);
    }

    @Bean
    public ConditionRevisionRepository conditionRevisionRepository(
            EntityManager entityManager,
            UserProvider userProvider
    ) {
        return new IamConditionRevisionRepository(
                new ConditionRevisionRepositoryImpl(entityManager),
                userProvider,
                entityManager);
    }

    @Bean
    public TaskRevisionRepository revisionTaskRepository(
            EntityManager entityManager
    ) {
        return new TaskRevisionRepositoryImpl(entityManager);
    }

    @Bean
    public UserProvider mockUserProvider() {
        return () -> new Pid("123456");
    }

    @Bean
    public UserRepository mockUserRepository() {
        return new UserRepository() {
            @Override
            public User findById(Pid aPid) {
                return new User() {
                    @Override
                    public boolean isApprover() {
                        return true;
                    }

                    @Override
                    public Country location() {
                        return new Country("FR");
                    }

                    @Override
                    public boolean hasBookingCenter(Country country) {
                        return false;
                    }
                };
            }
        };
    }
}
