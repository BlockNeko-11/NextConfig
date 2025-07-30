package io.github.blockneko11.nextconfig.util;

import org.jetbrains.annotations.Nullable;

public final class ReflectionUtils {
    @Nullable
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private ReflectionUtils() {
    }
}
