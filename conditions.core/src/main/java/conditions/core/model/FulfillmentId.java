package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class FulfillmentId extends LongId {

    public FulfillmentId() {
        super();
    }

    public FulfillmentId(Long id) {
        super(id);
    }
}
