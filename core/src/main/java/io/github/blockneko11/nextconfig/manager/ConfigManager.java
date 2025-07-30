package io.github.blockneko11.nextconfig.manager;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.option.ConfigOptions;
import lombok.Setter;

public abstract class ConfigManager<T extends Config> {
    protected final Class<T> clazz;

    private T value;
    @Setter
    protected ConfigOptions options = ConfigOptions.DEFAULT;

    protected ConfigManager(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get() {
        return this.value;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void set(T value) {
        this.value = value;
    }

    public abstract void load() throws Exception;

    public abstract void save() throws Exception;
}
