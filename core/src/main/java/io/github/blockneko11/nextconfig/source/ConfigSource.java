package io.github.blockneko11.nextconfig.source;

import io.github.blockneko11.nextconfig.throwable.ConfigException;
import org.jetbrains.annotations.NotNull;

public interface ConfigSource {
    @NotNull
    String read() throws ConfigException;

    void write(@NotNull String text) throws ConfigException;
}
