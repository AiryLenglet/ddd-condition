package conditions.micronaut.config;

import conditions.core.model.EncryptionConverter;
import conditions.core.service.Base64EncryptionService;
import conditions.core.service.EncryptionService;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;

@Factory
public class EncryptionFactory {

    @Bean
    public EncryptionService encryptionService() {
        return new Base64EncryptionService();
    }

    @EventListener
    public void onStartupEvent(StartupEvent startupEvent) {
        EncryptionConverter.Dependencies.INSTANCE.set(startupEvent.getSource().getBean(EncryptionService.class));
    }
}
