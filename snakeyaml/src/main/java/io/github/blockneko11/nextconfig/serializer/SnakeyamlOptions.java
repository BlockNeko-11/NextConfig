package io.github.blockneko11.nextconfig.serializer;

import io.github.blockneko11.nextconfig.util.Lazy;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

public interface SnakeyamlOptions {
    Lazy<LoaderOptions> LOADER_OPTIONS = Lazy.of(() -> {
        LoaderOptions options = new LoaderOptions();

        options.setProcessComments(false);

        return options;
    });

    Lazy<DumperOptions> DUMPER_OPTIONS = Lazy.of(() -> {
        DumperOptions options = new DumperOptions();

        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setProcessComments(false);
        options.setIndent(2);
        options.setIndicatorIndent(2);
        options.setIndentWithIndicator(true);
        options.setPrettyFlow(false);

        return options;
    });
}
