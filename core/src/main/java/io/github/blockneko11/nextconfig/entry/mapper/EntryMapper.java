package io.github.blockneko11.nextconfig.entry.mapper;

import org.jetbrains.annotations.NotNull;

public interface EntryMapper<T> {
    @NotNull
    Class<T> getEntryType();

    T toEntry(@NotNull Object property);

    Object toProperty(T entry);
}
