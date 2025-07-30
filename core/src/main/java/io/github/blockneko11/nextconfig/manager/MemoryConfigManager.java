package io.github.blockneko11.nextconfig.manager;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.util.ReflectionUtils;

public class MemoryConfigManager<T extends Config> extends ConfigManager<T> {
    public MemoryConfigManager(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public void load() throws Exception {
        this.set(ReflectionUtils.newInstance(this.clazz));
    }

    @Override
    public void save() throws Exception {
        this.set(null);
    }
}
