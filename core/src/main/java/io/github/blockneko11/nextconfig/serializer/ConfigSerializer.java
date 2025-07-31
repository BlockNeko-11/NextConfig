package io.github.blockneko11.nextconfig.serializer;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ConfigSerializer {
    @Nullable
    Map<String, Object> parse(String text);

    @NotNull
    String serialize(Map<String, Object> conf);
}
