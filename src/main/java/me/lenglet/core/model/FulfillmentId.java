package me.lenglet.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class FulfillmentId extends UuidId {

    public FulfillmentId(String id) {
        super(id);
    }

    public FulfillmentId() {
        super();
    }

    public static FulfillmentId of(String id) {
        return new FulfillmentId(id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
