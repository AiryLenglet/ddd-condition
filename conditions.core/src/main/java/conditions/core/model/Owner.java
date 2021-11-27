package conditions.core.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Owner {

    @Embedded
    private final Pid pid;

    private Owner() {
        this.pid = null;
    }

    public Owner(Pid pid) {
        if (pid == null) {
            throw new IllegalArgumentException("pid cannot be null");
        }
        this.pid = pid;
    }

    public Pid getPid() {
        return pid;
    }
}
