package io.github.blockneko11.nextconfig.holder;

import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.EntryIgnored;
import io.github.blockneko11.nextconfig.annotation.EntryName;
import io.github.blockneko11.nextconfig.option.ConfigOptions;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.source.ConfigSource;
import io.github.blockneko11.nextconfig.throwable.ConfigException;
import io.github.blockneko11.nextconfig.throwable.ConfigMappingException;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigHolder<T> {
    private final Class<T> clazz;
    private final ConfigSource source;
    private final ConfigSerializer serializer;
    private final ConfigOptions options;

    public ConfigHolder(Class<T> clazz,
                        ConfigSource source,
                        ConfigSerializer serializer,
                        ConfigOptions options) {
        if (!clazz.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("Class " +
                    clazz.getCanonicalName() +
                    " should be annotated with @Config");
        }

        this.clazz = clazz;
        this.source = source;
        this.serializer = serializer;
        this.options = options;
    }

    public ConfigHolder(Class<T> clazz,
                        ConfigSource source,
                        ConfigSerializer serializer) {
        this(clazz, source, serializer, new ConfigOptions());
    }

    private T config;

    public T getConfig() throws ConfigException {
        if (this.config == null) {
            this.load();
        }

        return this.config;
    }

    public void setConfig(T value) throws ConfigException {
        this.config = value;

        if (this.options.autoSave) {
            this.save();
        }
    }

    public void load() throws ConfigException {
        String text = this.source.read();
        Map<String, Object> map = this.serializer.parse(text);
        this.config = mapToObject(this.clazz, map, this.options);
    }

    public void save() throws ConfigException {
        Map<String, Object> map = objectToMap(this.clazz, this.config, this.options);
        String text = this.serializer.serialize(map);
        this.source.write(text);
    }

    private static <T> T mapToObject(Class<T> clazz, Map<String, Object> map, ConfigOptions options)
            throws ConfigMappingException {
        T instance = ReflectionUtils.newInstance(clazz);
        if (instance == null) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return instance;
        }

        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(EntryIgnored.class)) {
                continue;
            }

            int i = f.getModifiers();
            boolean bl = false;

            for (Integer mod : options.ignoreModifiers) {
                if ((i & mod) != 0) {
                    bl = true;
                    break;
                }
            }

            if (bl) {
                continue;
            }

            EntryName s = f.getAnnotation(EntryName.class);
            String key = s != null ? s.value() : f.getName();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            Object value = map.get(key);

            if (value == null) {
                continue;
            }

            Class<?> fType = f.getType();

            try {
                if (fType.isPrimitive()) {
                    if (fType == boolean.class) {
                        f.setBoolean(instance, (boolean) value);
                        continue;
                    }

                    if (fType == int.class) {
                        f.setInt(instance, ((Number) value).intValue());
                        continue;
                    }

                    if (fType == long.class) {
                        f.setLong(instance, ((Number) value).longValue());
                        continue;
                    }

                    if (fType == double.class) {
                        f.setDouble(instance, ((Number) value).doubleValue());
                        continue;
                    }
                }

                if (fType == String.class) {
                    f.set(instance, value);
                    continue;
                }

                if (fType == List.class) {
                    f.set(instance, value);
                    continue;
                }

                if (fType == Map.class) {
                    f.set(instance, value);
                    continue;
                }

                Object o = mapToObject(fType, (Map<String, Object>) value, options);
                f.set(instance, o);
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return instance;
    }

    private static Map<String, Object> objectToMap(Class<?> clazz, Object object, ConfigOptions options)
            throws ConfigMappingException {
        if (object == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(EntryIgnored.class)) {
                continue;
            }

            int i = f.getModifiers();
            boolean bl = false;

            for (Integer mod : options.ignoreModifiers) {
                if ((i & mod) != 0) {
                    bl = true;
                    break;
                }
            }

            if (bl) {
                continue;
            }

            EntryName s = f.getAnnotation(EntryName.class);
            String key = s != null ? s.value() : f.getName();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            try {
                map.put(key, f.get(object));
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return map;
    }
}
