package io.github.blockneko11.nextconfig.entry.mapper;

import org.jetbrains.annotations.NotNull;

public interface EntryMapper<T> {
    T toEntry(@NotNull Object property);

    @NotNull
    Object toProperty(T entry);
}
