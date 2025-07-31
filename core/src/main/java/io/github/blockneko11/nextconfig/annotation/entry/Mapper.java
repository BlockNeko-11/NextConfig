package io.github.blockneko11.nextconfig.annotation.entry;

import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mapper {
    Class<? extends EntryMapper<?>> value();
}
