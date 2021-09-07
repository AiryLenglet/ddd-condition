package me.lenglet.core.model;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public abstract class UuidId implements Serializable {

    protected final String id;

    protected UuidId(String id) {
        this.id = id;
    }

    protected UuidId() {
        this(UUID.randomUUID().toString());
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        return this.id.equals(((UuidId) obj).getId());
    }
}
