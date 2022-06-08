package conditions.core.model;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.function.Function;

public class EmbeddedLongIdSequenceStyleGenerator extends SequenceStyleGenerator {

    private Function<Long, Serializable> wrapper;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        if (LongId.class.isAssignableFrom(type.getReturnedClass())) {
            final var initialType = type;
            type = ((ComponentType) type).getSubtypes()[0];
            final Constructor constructor;
            try {
                constructor = initialType.getReturnedClass().getConstructor(Long.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            this.wrapper = l -> {
                try {
                    return (Serializable) constructor.newInstance(l);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
        super.configure(type, params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        final var newId = super.generate(session, object);
        return wrapper == null ? newId : wrapper.apply((Long) newId);
    }
}
