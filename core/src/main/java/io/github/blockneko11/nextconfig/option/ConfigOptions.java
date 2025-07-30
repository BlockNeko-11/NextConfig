package io.github.blockneko11.nextconfig.option;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class ConfigOptions {
    public static final ConfigOptions DEFAULT = new ConfigOptions();

    public List<Integer> ignoreModifiers = Arrays.asList(
            Modifier.STATIC,
            Modifier.FINAL,
            Modifier.TRANSIENT
    );

    public boolean parse_nameIgnoreCase = true;

    public boolean serialize_ignoreNullValues = true;
}
