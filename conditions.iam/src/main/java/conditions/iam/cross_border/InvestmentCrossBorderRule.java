package conditions.iam.cross_border;

import conditions.core.model.Country;

import java.util.Set;

public class InvestmentCrossBorderRule {

    private InvestmentCrossBorderRule() {
    }

    public static Iterable<Country> canNotSee(Country aCountry) {
        if ("RU".equals(aCountry.getCode())) {
            return Set.of(new Country("SA"));
        }
        return Set.of();
    }
}
