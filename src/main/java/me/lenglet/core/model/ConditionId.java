package me.lenglet.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class ConditionId extends UuidId {

    public ConditionId(String id) {
        super(id);
    }

    public ConditionId() {
        super();
    }

    public static ConditionId of(String id) {
        return new ConditionId(id);
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
