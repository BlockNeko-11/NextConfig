package io.github.blockneko11.nextconfig.manager;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.util.FileUtils;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
        Map<String, Object> map = this.serializer.read(text);
        this.set(ReflectionUtils.toObject(this.clazz, map));
    }

    @Override
    public void save() throws IOException {
        Map<String, Object> map = ReflectionUtils.toMap(this.clazz, this.get());
        String text = this.serializer.write(map);
        FileUtils.write(this.file, text);
    }
}
