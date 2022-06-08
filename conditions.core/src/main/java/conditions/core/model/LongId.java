package conditions.core.model;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class LongId implements Serializable {

    protected Long id;

    public Long getId() {
        return id;
    }

    public LongId(Long id) {
        this.id = id;
    }

    LongId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongId longId = (LongId) o;
        return Objects.equals(getId(), longId.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
