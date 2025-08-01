package io.github.blockneko11.nextconfig.holder;

import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.entry.Ignored;
import io.github.blockneko11.nextconfig.annotation.entry.Name;
import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;
import io.github.blockneko11.nextconfig.properties.ConfigProperties;
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
    private final ConfigProperties props;

    public ConfigHolder(Class<T> clazz,
                        ConfigSource source,
                        ConfigSerializer serializer,
                        ConfigProperties props) {
        checkClass(clazz);
        this.clazz = clazz;
        this.source = source;
        this.serializer = serializer;
        this.props = props;
    }

    public ConfigHolder(Class<T> clazz,
                        ConfigSource source,
                        ConfigSerializer serializer) {
        this(clazz, source, serializer, new ConfigProperties());
    }

    private static void checkClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("Class " +
                    clazz.getCanonicalName() +
                    " should be annotated with @Config");
        }
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
    }

    public void load() throws ConfigException {
        String text = this.source.read();
        Map<String, Object> map = this.serializer.parse(text);
        this.config = mapToObject(this.clazz, map, this.props);
    }

    public void save() throws ConfigException {
        Map<String, Object> map = objectToMap(this.clazz, this.config, this.props);
        String text = this.serializer.serialize(map);
        this.source.write(text);
    }

    private static <T> T mapToObject(Class<T> clazz, Map<String, Object> map, ConfigProperties props)
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

            if (props.hasIgnoredModifier(f.getModifiers())) {
                continue;
            }

            Name s = f.getAnnotation(Name.class);
            String key = s == null ? f.getName() : s.value();
            Class<?> type = f.getType();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            Object property = map.get(key);

            try {
                if (property == null) {
                    if (type == Optional.class) {
                        f.set(instance, Optional.empty());
                    }

                    continue;
                }

                if (type.isArray()) {
                    throw new ConfigMappingException("Array is not supported");
                }

                if (type == boolean.class) {
                    f.setBoolean(instance, (boolean) property);
                    continue;
                }

                if (type == int.class) {
                    f.setInt(instance, ((Number) property).intValue());
                    continue;
                }

                if (type == long.class) {
                    f.setLong(instance, ((Number) property).longValue());
                    continue;
                }

                if (type == double.class) {
                    f.setDouble(instance, ((Number) property).doubleValue());
                    continue;
                }

                if (type == Integer.class) {
                    f.set(instance, ((Number) property).intValue());
                    continue;
                }

                if (type == Long.class) {
                    f.set(instance, ((Number) property).longValue());
                    continue;
                }

                if (type == Double.class) {
                    f.set(instance, ((Number) property).doubleValue());
                    continue;
                }

                if (type == Boolean.class ||
                        type == String.class ||
                        type == List.class ||
                        type == Map.class) {
                    f.set(instance, property);
                    continue;
                }

                if (type.isEnum()) {
                    f.set(instance, Enum.valueOf((Class<Enum>) type, (String) property));
                    continue;
                }

                if (type == Optional.class) {
                    f.set(instance, Optional.of(property));
                    continue;
                }

                if (props.isMapperRegistered(type)) {
                    EntryMapper<?> mapper = props.getMapper(type);
                    if (mapper == null) {
                        throw new ConfigMappingException("Type " +
                                type.getCanonicalName() +
                                " has no registered mappers");
                    }

                    f.set(instance, mapper.toEntry(property));
                    continue;
                }

                Object entry = mapToObject(type, (Map<String, Object>) property, props);
                f.set(instance, entry);
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return instance;
    }

    private static Map<String, Object> objectToMap(Class<?> clazz, Object object, ConfigProperties props)
            throws ConfigMappingException {
        if (object == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Ignored.class)) {
                continue;
            }

            if (props.hasIgnoredModifier(f.getModifiers())) {
                continue;
            }

            Name s = f.getAnnotation(Name.class);
            String key = s == null ? f.getName() : s.value();
            Class<?> type = f.getType();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            try {
                if (type.isArray()) {
                    throw new ConfigMappingException("Array is not supported");
                }

                if (type == boolean.class) {
                    map.put(key, f.getBoolean(object));
                    continue;
                }

                if (type == int.class) {
                    map.put(key, f.getInt(object));
                    continue;
                }

                if (type == long.class) {
                    map.put(key, f.getLong(object));
                    continue;
                }

                if (type == double.class) {
                    map.put(key, f.getDouble(object));
                    continue;
                }

                Object entry = f.get(object);

                if (type == Boolean.class ||
                        type == Integer.class ||
                        type == Long.class ||
                        type == Double.class ||
                        type == String.class ||
                        type == List.class ||
                        type == Map.class) {
                    map.put(key, entry);
                    continue;
                }

                if (type.isEnum()) {
                    map.put(key, entry.toString());
                    continue;
                }

                if (type == Optional.class) {
                    Optional<?> optional = (Optional<?>) entry;
                    map.put(key, optional.orElse(null));
                    continue;
                }

                if (props.isMapperRegistered(type)) {
                    EntryMapper mapper = props.getMapper(type);
                    if (mapper == null) {
                        throw new ConfigMappingException("Type " +
                                type.getCanonicalName() +
                                " has no registered mappers");
                    }

                    map.put(key, mapper.toProperty(entry));
                    continue;
                }

                Object property = objectToMap(type, entry, props);
                map.put(key, property);
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return map;
    }
}
