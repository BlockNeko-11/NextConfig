package io.github.blockneko11.nextconfig.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.Map;

public final class GsonConfigSerializer implements ConfigSerializer {
    public static final ConfigSerializer DEFAULT = new GsonConfigSerializer();

    private final Gson gson;

    public GsonConfigSerializer() {
        this(new GsonBuilder().serializeNulls().setPrettyPrinting().create());
    }

    public GsonConfigSerializer(GsonBuilder builder) {
        this(builder.create());
    }

    public GsonConfigSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Map<String, Object> read(String text) {
        try {
            return (Map<String, Object>) this.gson.fromJson(text, Map.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @Override
    public String write(Map<String, Object> conf) {
        return this.gson.toJson(conf);
    }
}
