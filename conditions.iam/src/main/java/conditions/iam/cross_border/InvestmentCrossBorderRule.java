package conditions.iam.cross_border;

import conditions.core.model.Country;

import java.util.Collections;
import java.util.LinkedList;

public class InvestmentCrossBorderRule {

    private InvestmentCrossBorderRule() {
    }

    public static Iterable<Country> canNotSee(Country aCountry) {
        if (aCountry == null) {
            return Collections.emptyList();
        }

        final var excludedCountries = new LinkedList<Country>();
        if ("RU".equals(aCountry.getCode())) {
            excludedCountries.add(new Country("SA"));
        }

        if (!"CH".equals(aCountry.getCode()) && !"FR".equals(aCountry.getCode())) {
            excludedCountries.add(new Country("FR"));
        }

        if (!"BE".equals(aCountry.getCode())) {
            excludedCountries.add(new Country("BE"));
        }

        return excludedCountries;
    }
}
