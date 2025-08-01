package io.github.blockneko11.nextconfig.entry.mapper;

import io.github.blockneko11.nextconfig.throwable.ConfigMappingException;
import org.jetbrains.annotations.NotNull;

public interface EntryMapper<T> {
    @NotNull
    Class<T> getEntryType();

    T toEntry(@NotNull Object property) throws ConfigMappingException;

    Object toProperty(T entry) throws ConfigMappingException;
}
