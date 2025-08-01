package io.github.blockneko11.nextconfig.holder;

import io.github.blockneko11.nextconfig.annotation.entry.Name;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class EntryData {
    private final String key;
    private final Class<?> type;
    private final int modifiers;

    // read-only collections
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public EntryData(Field field) {
        Name s = field.getAnnotation(Name.class);
        this.key = s != null ? s.value() : field.getName();

        this.type = field.getType();
        this.modifiers = field.getModifiers();
        this.annotations = Arrays.stream(field.getDeclaredAnnotations())
                .collect(Collectors.toMap(Annotation::annotationType, a -> a));
    }
}
