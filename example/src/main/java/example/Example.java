package example;

import io.github.blockneko11.nextconfig.holder.ConfigHolder;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;
import io.github.blockneko11.nextconfig.source.FileConfigSource;
import io.github.blockneko11.nextconfig.throwable.ConfigException;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;

public final class Example {
    public static void file(ConfigSerializer serializer, String extension) throws ConfigException {
        File f = new File("run", "config." + extension);

        ConfigHolder<ExampleConfig> holder = new ConfigHolder<>(
                ExampleConfig.class,
                new FileConfigSource(f),
                serializer
        );

        // before load
        System.out.println(holder.getConfig());

        // load
        holder.load();
        ExampleConfig config1 = holder.getConfig();
        System.out.println(config1.a_boolean);
        System.out.println(config1.an_int);
        System.out.println(config1.a_string);
        System.out.println(config1.a_double);
        System.out.println(config1.a_list);
        System.out.println(config1.a_map);
        System.out.println(config1.ignored_field);
        System.out.println(config1.b_string);
        System.out.println(config1.a_null);
        System.out.println(config1.a_enum);

        config1.a_boolean = false;
        config1.an_int = 114514;
        config1.a_string = "This is not a string!";
        config1.a_double = 2.71828;
        config1.a_list = Arrays.asList("This", "is", "not", "a", "list");
        config1.a_map = new LinkedHashMap<>();
        config1.a_map.put("key", "value");
        config1.ignored_field = "This field will not be ignored";
        config1.b_string = "This string will not be serialized as 'b_string'";
        config1.a_null = null;
        config1.a_enum = ExampleConfig.State.OFF;

        // save
        holder.save();

        // reload
        holder.load();
        ExampleConfig config2 = holder.getConfig();
        System.out.println(config2.a_boolean);
        System.out.println(config2.an_int);
        System.out.println(config2.a_string);
        System.out.println(config2.a_double);
        System.out.println(config2.a_list);
        System.out.println(config2.a_map);
        System.out.println(config2.ignored_field);
        System.out.println(config2.b_string);
        System.out.println(config2.a_null);
        System.out.println(config2.a_enum);

        holder.save();
    }
}
