package io.github.blockneko11.nextconfig.option;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigOptions {
    public boolean autoSave = true;

    public Set<Integer> ignoreModifiers = new HashSet<>(Arrays.asList(
            Modifier.STATIC,
            Modifier.FINAL,
            Modifier.TRANSIENT
    ));
}
