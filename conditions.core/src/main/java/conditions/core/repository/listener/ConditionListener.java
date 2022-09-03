package conditions.core.repository.listener;

import conditions.core.factory.Clock;
import conditions.core.model.Condition;
import conditions.core.model.ConditionRevision;
import conditions.core.repository.ConditionRevisionRepository;
import jakarta.inject.Singleton;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Singleton
public class ConditionListener {

    private final ConditionRevisionRepository conditionRevisionRepository;
    private final Clock clock;

    public ConditionListener(
            ConditionRevisionRepository conditionRevisionRepository,
            Clock clock
    ) {
        this.conditionRevisionRepository = conditionRevisionRepository;
        this.clock = clock;
    }

    @PostPersist
    @PostUpdate
    public void postPersist(Condition condition) {

        this.conditionRevisionRepository.persist(new ConditionRevision(
                condition.getConditionId(),
                condition.getOwner(),
                condition.getVersion(),
                this.clock.now()
        ));
    }
}
