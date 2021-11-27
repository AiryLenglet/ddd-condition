package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class Pid {

    private final String value;

    private Pid() {
        this.value = null;
    }

    public Pid(String value) {
        if(!isValid(value)) {
            throw new IllegalArgumentException("Invalid value '" + value + "'");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String aPid) {
        if (aPid == null) {
            return false;
        }
        return aPid.length() == 6;
    }
}
