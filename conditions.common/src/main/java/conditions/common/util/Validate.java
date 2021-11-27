package conditions.common.util;

import java.util.function.Supplier;

public final class Validate {

    private Validate() {
    }

    public static void notNull(Object o) {
        notNull(o, () -> new NullPointerException("Object cannot be null"));
    }

    public static void notNull(Object o, Supplier<RuntimeException> exceptionSupplier) {
        if (o == null) {
            throw exceptionSupplier.get();
        }
    }
}
