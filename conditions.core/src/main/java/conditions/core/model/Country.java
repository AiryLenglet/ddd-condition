package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class Country {

    private final String code;

    private Country() {
        this.code = null;
    }

    public Country(
            String code
    ) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
