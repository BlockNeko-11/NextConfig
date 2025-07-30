package io.github.blockneko11.nextconfig.manager;

import io.github.blockneko11.nextconfig.Config;

public abstract class ConfigManager<T extends Config> {
    protected final Class<T> clazz;

    protected ConfigManager(Class<T> clazz) {
        this.clazz = clazz;
    }

    private T value;

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public abstract void load() throws Exception;

    public abstract void save() throws Exception;
}
