package io.github.blockneko11.nextconfig.holder;

import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.entry.Ignored;
import io.github.blockneko11.nextconfig.annotation.entry.Mapper;
import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.source.ConfigSource;
import io.github.blockneko11.nextconfig.throwable.ConfigException;
import io.github.blockneko11.nextconfig.throwable.ConfigMappingException;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConfigHolder<T> {
    private final Class<T> clazz;
    private final ConfigSource source;
    private final ConfigSerializer serializer;
    private final Map<Field, EntryData> entryCache = new HashMap<>();

    public ConfigHolder(Class<T> clazz,
                        ConfigSource source,
                        ConfigSerializer serializer) {
        checkClass(clazz);
        this.clazz = clazz;
        this.source = source;
        this.serializer = serializer;
        this.scanEntries();
    }

    private static void checkClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("Class " +
                    clazz.getCanonicalName() +
                    " should be annotated with @Config");
        }
    }

    private void scanEntries() {
        for (Field f : this.clazz.getDeclaredFields()) {
            this.entryCache.put(f, new EntryData(f));
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
        this.config = mapToObject(this.clazz, map, this.entryCache);
    }

    public void save() throws ConfigException {
        Map<String, Object> map = objectToMap(this.clazz, this.config, this.entryCache);
        String text = this.serializer.serialize(map);
        this.source.write(text);
    }

    private static <T> T mapToObject(Class<T> clazz, Map<String, Object> map, Map<Field, EntryData> entries)
            throws ConfigMappingException {
        T instance = ReflectionUtils.newInstance(clazz);
        if (instance == null) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return instance;
        }

        for (Field f : clazz.getDeclaredFields()) {
            EntryData data = entries.get(f);
            Map<Class<? extends Annotation>, Annotation> annotations = data.getAnnotations();
            if (annotations.containsKey(Ignored.class)) {
                continue;
            }

            int mod = data.getModifiers();
            if (Modifier.isFinal(mod) || Modifier.isTransient(mod)) {
                continue;
            }

            String key = data.getKey();
            Class<?> type = data.getType();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            Object value = map.get(key);

            try {
                if (value == null) {
                    if (type == Optional.class) {
                        f.set(instance, Optional.empty());
                    }

                    continue;
                }

                if (type.isArray()) {
                    throw new ConfigMappingException("Array is not supported");
                }

                if (type == boolean.class) {
                    f.setBoolean(instance, (boolean) value);
                    continue;
                }

                if (type == int.class) {
                    f.setInt(instance, ((Number) value).intValue());
                    continue;
                }

                if (type == long.class) {
                    f.setLong(instance, ((Number) value).longValue());
                    continue;
                }

                if (type == double.class) {
                    f.setDouble(instance, ((Number) value).doubleValue());
                    continue;
                }

                if (type == Integer.class) {
                    f.set(instance, ((Number) value).intValue());
                    continue;
                }

                if (type == Long.class) {
                    f.set(instance, ((Number) value).longValue());
                    continue;
                }

                if (type == Double.class) {
                    f.set(instance, ((Number) value).doubleValue());
                    continue;
                }

                if (type == Boolean.class ||
                        type == String.class ||
                        type == List.class ||
                        type == Map.class) {
                    f.set(instance, value);
                    continue;
                }

                if (type.isEnum()) {
                    f.set(instance, Enum.valueOf((Class<Enum>) type, (String) value));
                    continue;
                }

                if (type == Optional.class) {
                    f.set(instance, Optional.of(value));
                    continue;
                }

                if (annotations.containsKey(Mapper.class)) {
                    Class<? extends EntryMapper<?>> mapperClass = ((Mapper) annotations.get(Mapper.class))
                            .value();
                    EntryMapper<?> mapper = ReflectionUtils.newInstance(mapperClass);
                    if (mapper == null) {
                        throw new ConfigMappingException("Mapper " +
                                mapperClass.getCanonicalName() +
                                " is not found");
                    }

                    f.set(instance, mapper.toEntry(value));
                    continue;
                }

                Object o = mapToObject(type, (Map<String, Object>) value, entries);
                f.set(instance, o);
            } catch (IllegalAccessException e) {
                throw new ConfigMappingException(e);
            }
        }

        return instance;
    }

    private static Map<String, Object> objectToMap(Class<?> clazz, Object object, Map<Field, EntryData> entries)
            throws ConfigMappingException {
        if (object == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : clazz.getDeclaredFields()) {
            EntryData data = entries.get(f);
            Map<Class<? extends Annotation>, Annotation> annotations = data.getAnnotations();
            if (annotations.containsKey(Ignored.class)) {
                continue;
            }

            int mod = data.getModifiers();
            if (Modifier.isFinal(mod) || Modifier.isTransient(mod)) {
                continue;
            }

            String key = data.getKey();
            Class<?> type = data.getType();

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            try {
                if (type.isArray()) {
                    throw new ConfigMappingException("Array is not supported");
                }

                if (type.isEnum()) {
                    map.put(key, f.get(object).toString());
                    continue;
                }

                if (type == Optional.class) {
                    Optional<?> optional = (Optional<?>) f.get(object);
                    map.put(key, optional.orElse(null));
                    continue;
                }

                if (annotations.containsKey(Mapper.class)) {
                    Class<? extends EntryMapper<?>> mapperClass = ((Mapper) annotations.get(Mapper.class))
                            .value();
                    EntryMapper mapper = ReflectionUtils.newInstance(mapperClass);
                    if (mapper == null) {
                        throw new ConfigMappingException("Mapper " +
                                mapperClass.getCanonicalName() +
                                " is not found");
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
