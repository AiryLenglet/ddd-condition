package conditions.iam.model;

import conditions.core.model.Country;

public interface User {
    boolean isApprover();
    Country location();
    boolean hasBookingCenter(Country country);
}
