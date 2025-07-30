package io.github.blockneko11.nextconfig.serializer;


import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ConfigSerializer {
    @Nullable
    Map<String, Object> read(String text);

    String write(Map<String, Object> conf);
}
