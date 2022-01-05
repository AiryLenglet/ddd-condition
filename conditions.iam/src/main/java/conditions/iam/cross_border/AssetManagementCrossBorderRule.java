package conditions.iam.cross_border;

import conditions.core.model.Country;

import java.util.Set;

public class AssetManagementCrossBorderRule {

    private AssetManagementCrossBorderRule() {
    }

    public static Iterable<Country> canSee(Country aCountry) {
        return Set.of(aCountry);
    }
}
