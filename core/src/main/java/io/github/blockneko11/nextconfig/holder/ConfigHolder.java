package io.github.blockneko11.nextconfig.holder;

import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.entry.Ignored;
import io.github.blockneko11.nextconfig.annotation.entry.Mapper;
import io.github.blockneko11.nextconfig.annotation.entry.Name;
import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;
import io.github.blockneko11.nextconfig.option.ConfigOptions;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.source.ConfigSource;
import io.github.blockneko11.nextconfig.throwable.ConfigException;
import io.github.blockneko11.nextconfig.throwable.ConfigMappingException;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

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
            if (f.isAnnotationPresent(Ignored.class)) {
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

            Name s = f.getAnnotation(Name.class);
            String key = s != null ? s.value() : f.getName();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            Object value = map.get(key);
            Class<?> fType = f.getType();

            try {
                if (value == null) {
                    if (fType == Optional.class) {
                        f.set(instance, Optional.empty());
                    }

                    continue;
                }

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

                if (fType == Integer.class) {
                    f.set(instance, ((Number) value).intValue());
                    continue;
                }

                if (fType == Long.class) {
                    f.set(instance, ((Number) value).longValue());
                    continue;
                }

                if (fType == Double.class) {
                    f.set(instance, ((Number) value).doubleValue());
                    continue;
                }

                if (fType == Boolean.class ||
                        fType == String.class ||
                        fType == List.class ||
                        fType == Map.class) {
                    f.set(instance, value);
                    continue;
                }

                if (fType.isEnum()) {
                    f.set(instance, Enum.valueOf((Class<Enum>) fType, (String) value));
                    continue;
                }

                if (fType == Optional.class) {
                    f.set(instance, Optional.of(value));
                    continue;
                }

                Mapper m = f.getAnnotation(Mapper.class);
                if (m != null) {
                    EntryMapper<?> mapper = ReflectionUtils.newInstance(m.value());

                    if (mapper == null) {
                        throw new ConfigMappingException("Mapper " + m.value().getCanonicalName() + " is not found");
                    }

                    f.set(instance, mapper.toEntry(value));
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
            if (f.isAnnotationPresent(Ignored.class)) {
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

            Name s = f.getAnnotation(Name.class);
            String key = s != null ? s.value() : f.getName();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            Class<?> fType = f.getType();

            try {
                if (fType.isEnum()) {
                    map.put(key, f.get(object).toString());
                    continue;
                }

                if (fType == Optional.class) {
                    Optional<?> optional = (Optional<?>) f.get(object);
                    map.put(key, optional.orElse(null));
                    continue;
                }

                Mapper m = f.getAnnotation(Mapper.class);
                if (m != null) {
                    EntryMapper mapper = ReflectionUtils.newInstance(m.value());

                    if (mapper == null) {
                        throw new ConfigMappingException("Mapper " + m.value().getName() + " is not found");
                    }

                    map.put(key, mapper.toProperty(f.get(object)));
                    continue;
                }

                map.put(key, f.get(object));
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return map;
    }
}
