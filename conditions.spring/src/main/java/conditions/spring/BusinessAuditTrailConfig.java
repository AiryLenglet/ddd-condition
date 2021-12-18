package conditions.spring;

import conditions.business_audit_trail.repository.BusinessAuditTrailConditionRepository;
import conditions.core.event.EventBus;
import conditions.core.event.condition.OwnerChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BusinessAuditTrailConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Subscribing to events for business audit trail");
        this.eventBus.subscribeForAfterCommit(OwnerChangedEvent.class, event -> {
            businessAuditTrail.save(String.format("Owner changed from %s to %s", event.getPreviousOwner(), event.getNewOwner()));
        });
    }

    @Bean
    public BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail() {
        return businessAuditTrailMessage -> log.info("BAT -> " + businessAuditTrailMessage);
    }
}
