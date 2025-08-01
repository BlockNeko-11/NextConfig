package io.github.blockneko11.nextconfig.properties;

import io.github.blockneko11.nextconfig.entry.mapper.DefaultMappers;
import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigProperties {
    private final List<EntryMapper<?>> mappers = new ArrayList<>();

    public void registerMapper(@NotNull EntryMapper<?> mapper) {
        this.mappers.add(mapper);
    }

    public boolean isMapperRegistered(@NotNull Class<?> clazz) {
        return this.mappers.stream()
                .anyMatch(mapper -> mapper.getEntryType() == clazz);
    }

    @Nullable
    public <T> EntryMapper<T> getMapper(@NotNull Class<T> clazz) {
        return (EntryMapper<T>) this.mappers.stream()
                .filter(mapper -> mapper.getEntryType() == clazz)
                .findFirst()
                .orElse(null);
    }

    @Getter
    @Setter
    private boolean autoSave = false;

    private final Set<Integer> ignoreModifiers = new HashSet<>();

    public void registerIgnoredModifier(int modifier) {
        this.ignoreModifiers.add(modifier);
    }

    public boolean hasIgnoredModifier(int modifiers) {
        return this.ignoreModifiers.stream()
                .anyMatch(i -> (modifiers & i) != 0);
    }

    public ConfigProperties() {
        this.registerMapper(new DefaultMappers.BigDecimalMapper());
        this.registerMapper(new DefaultMappers.BigIntegerMapper());

        this.registerIgnoredModifier(Modifier.FINAL);
        this.registerIgnoredModifier(Modifier.TRANSIENT);
    }
}
