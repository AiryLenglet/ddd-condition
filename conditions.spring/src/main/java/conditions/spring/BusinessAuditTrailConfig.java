package conditions.spring;

import conditions.business_audit_trail.repository.BusinessAuditTrailConditionRepository;
import conditions.core.event.EventBus;
import conditions.core.event.condition.OwnerChangedEvent;
import conditions.spring.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BusinessAuditTrailConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAuditTrailConfig.class);

    @Autowired
    private BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        LOGGER.info("Subscribing to events for business audit trail");
        this.eventBus.subscribeForAfterCommit(OwnerChangedEvent.class, event -> {
            businessAuditTrail.save(String.format("Owner changed from %s to %s", event.getPreviousOwner(), event.getNewOwner()));
        });
    }

    @Bean
    public BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail() {
        return businessAuditTrailMessage -> LOGGER.info("BAT -> " + businessAuditTrailMessage);
    }
}
