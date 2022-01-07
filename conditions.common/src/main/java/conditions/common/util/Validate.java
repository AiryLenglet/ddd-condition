package conditions.common.util;

import java.util.Arrays;
import java.util.function.Supplier;

public final class Validate {

    private Validate() {
    }

    public static <T> T notNull(T o) {
        return notNull(o, () -> new NullPointerException("Object cannot be null"));
    }

    public static <T> T notNull(T o, Supplier<RuntimeException> exceptionSupplier) {
        if (o == null) {
            throw exceptionSupplier.get();
        }
        return o;
    }

    public static <E extends Enum<E>> E valid(Class<E> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknow value '" + value + "' for enum " + enumClass));
    }
}
