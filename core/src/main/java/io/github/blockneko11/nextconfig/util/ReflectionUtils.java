package io.github.blockneko11.nextconfig.util;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.annotation.SerializeIgnored;
import io.github.blockneko11.nextconfig.annotation.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectionUtils {
    @Nullable
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends Config> T toObject(Class<T> clazz, Map<String, Object> map) {
        T instance = newInstance(clazz);

        if (instance == null) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return instance;
        }

        for (Field f : clazz.getDeclaredFields()) {
            // annotation
            if (f.isAnnotationPresent(SerializeIgnored.class)) {
                continue;
            }

            // modifier
            int i = f.getModifiers();

            if (Modifier.isStatic(i) || Modifier.isFinal(i) || Modifier.isTransient(i)) {
                continue;
            }

            // access
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            // serialized name
            SerializedName sName = f.getAnnotation(SerializedName.class);
            String name = sName != null ? sName.value() : f.getName();

            // query & set
            Object o = map.get(name);

            if (o == null) {
                continue;
            }

            Class<?> fType = f.getType();

            // primitive types - int, long, double, boolean
            try {
                if (fType.isPrimitive()) {
                    if (fType == boolean.class) {
                        f.setBoolean(instance, (boolean) o);
                        continue;
                    }

                    if (fType == int.class) {
                        f.setInt(instance, ((Number) o).intValue());
                        continue;
                    }

                    if (fType == long.class) {
                        f.setLong(instance, ((Number) o).longValue());
                        continue;
                    }

                    if (fType == double.class) {
                        f.setDouble(instance, ((Number) o).doubleValue());
                        continue;
                    }
                }

                // string
                if (fType == String.class) {
                    f.set(instance, o);
                    continue;
                }

                // list
                if (fType == List.class) {
                    f.set(instance, o);
                    continue;
                }

                // map
                if (fType == Map.class) {
                    f.set(instance, o);
                    continue;
                }

                // objects (WIP)
                throw new UnsupportedOperationException("Objects are not supported yet.");
            } catch (IllegalAccessException ignored) {
            }
        }

        return instance;
    }

    public static <T extends Config> Map<String, Object> toMap(Class<T> clazz, T instance) {
        if (instance == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : clazz.getDeclaredFields()) {
            // annotation
            if (f.isAnnotationPresent(SerializeIgnored.class)) {
                continue;
            }

            // modifier
            int i = f.getModifiers();

            if (Modifier.isStatic(i) || Modifier.isFinal(i) || Modifier.isTransient(i)) {
                continue;
            }

            // access
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            // serialized name
            SerializedName sName = f.getAnnotation(SerializedName.class);
            String name = sName != null ? sName.value() : f.getName();

            // put
            try {
                map.put(name, f.get(instance));
            } catch (IllegalAccessException ignored) {
            }
        }

        return map;
    }

    private ReflectionUtils() {
    }
}
