package io.github.blockneko11.nextconfig.manager;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.annotation.PropertyIgnored;
import io.github.blockneko11.nextconfig.annotation.PropertyName;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.util.FileUtils;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileConfigManager<T extends Config> extends ConfigManager<T> {
    private final ConfigSerializer serializer;
    private final File file;

    public FileConfigManager(Class<T> clazz, ConfigSerializer serializer, File file) {
        super(clazz);
        this.serializer = serializer;
        this.file = file;
    }

    public FileConfigManager(Class<T> clazz, ConfigSerializer serializer, Path path) {
        this(clazz, serializer, path.toFile());
    }

    public FileConfigManager(Class<T> clazz, ConfigSerializer serializer, String path) {
        this(clazz, serializer, new File(path));
    }

    @Override
    public void load() throws IOException {
        String text = FileUtils.read(this.file);
        Map<String, Object> map = this.serializer.parse(text);
        this.set(toObject(map));
    }

    @Override
    public void save() throws IOException {
        Map<String, Object> map = this.toMap();
        String text = this.serializer.serialize(map);
        FileUtils.write(this.file, text);
    }

    private T toObject(Map<String, Object> map) {
        T instance = ReflectionUtils.newInstance(this.clazz);

        if (instance == null) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return instance;
        }

        for (Field f : this.clazz.getDeclaredFields()) {
            // annotation
            if (f.isAnnotationPresent(PropertyIgnored.class)) {
                continue;
            }

            // modifier
            int mod = f.getModifiers();
            boolean hasAnyIgnoredModifiers = false;

            for (int i : this.options.ignoreModifiers) {
                if ((mod & i) != 0) {
                    hasAnyIgnoredModifiers = true;
                    break;
                }
            }

            if (hasAnyIgnoredModifiers) {
                continue;
            }

            // access
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            // serialized name
            PropertyName sName = f.getAnnotation(PropertyName.class);
            String name = sName != null ? sName.value() : f.getName();

            if (this.options.parse_nameIgnoreCase) {
                name = name.toLowerCase();
            }

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

    private Map<String, Object> toMap() {
        if (!this.isPresent()) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : this.clazz.getDeclaredFields()) {
            // annotation
            if (f.isAnnotationPresent(PropertyIgnored.class)) {
                continue;
            }

            // modifier
            int mod = f.getModifiers();
            boolean hasAnyIgnoredModifiers = false;

            for (int i : this.options.ignoreModifiers) {
                if ((mod & i) != 0) {
                    hasAnyIgnoredModifiers = true;
                    break;
                }
            }

            if (hasAnyIgnoredModifiers) {
                continue;
            }

            // access
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            // serialized name
            PropertyName sName = f.getAnnotation(PropertyName.class);
            String name = sName != null ? sName.value() : f.getName();

            // put
            try {
                Object value = f.get(this.get());

                if (value == null && this.options.serialize_ignoreNullValues) {
                    continue;
                }

                map.put(name, value);
            } catch (IllegalAccessException ignored) {
            }
        }

        return map;
    }
}
