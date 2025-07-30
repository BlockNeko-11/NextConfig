package io.github.blockneko11.nextconfig.serializer;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.Map;

public final class SnakeyamlConfigSerializer implements ConfigSerializer {
    public static final ConfigSerializer DEFAULT = new SnakeyamlConfigSerializer();

    private final Yaml yaml;

    public SnakeyamlConfigSerializer() {
        this(new Yaml(SnakeyamlOptions.LOADER_OPTIONS.get(), SnakeyamlOptions.DUMPER_OPTIONS.get()));
    }

    public SnakeyamlConfigSerializer(Yaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Map<String, Object> parse(String text) {
        return this.yaml.load(text);
    }

    @Override
    public String serialize(Map<String, Object> conf) {
        return this.yaml.dumpAs(conf, Tag.MAP, null);
    }
}
