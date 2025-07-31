package example;

import io.github.blockneko11.nextconfig.option.FileConfigOptions;
import io.github.blockneko11.nextconfig.serializer.ConfigSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

public final class Example {
    public static void memory() throws Exception {
//        MemoryConfigManager<ExampleConfig> manager = new MemoryConfigManager<>(ExampleConfig.class);
//
//        // before load
//        System.out.println(manager.get());
//
//        // load
//        manager.load();
//        ExampleConfig config = manager.get();
//        System.out.println(config.a_boolean);
//        System.out.println(config.an_int);
//        System.out.println(config.a_string);
//        System.out.println(config.a_double);
//        System.out.println(config.a_list);
//        System.out.println(config.a_map);
//        System.out.println(config.ignored_field);
//        System.out.println(config.b_string);
//        System.out.println(config.a_null);
//
//        // save
//        manager.save();
//        System.out.println(manager.get());
    }

    public static void file(ConfigSerializer serializer, String extension) throws IOException {
//        File f = new File("run", "config." + extension);
//
//        FileConfigManager<ExampleConfig> manager = new FileConfigManager<>(
//                ExampleConfig.class,
//                serializer,
//                f
//        );
//
//        manager.setOptions(FileConfigOptions.loadFromClasspath());
//
//        // before load
//        System.out.println(manager.get());
//
//        // load
//        manager.load();
//        ExampleConfig config1 = manager.get();
//        System.out.println(config1.a_boolean);
//        System.out.println(config1.an_int);
//        System.out.println(config1.a_string);
//        System.out.println(config1.a_double);
//        System.out.println(config1.a_list);
//        System.out.println(config1.a_map);
//        System.out.println(config1.ignored_field);
//        System.out.println(config1.b_string);
//        System.out.println(config1.a_null);
//
//        config1.a_boolean = false;
//        config1.an_int = 114514;
//        config1.a_string = "This is not a string!";
//        config1.a_double = 2.71828;
//        config1.a_list = Arrays.asList("This", "is", "not", "a", "list");
//        config1.a_map = new LinkedHashMap<>();
//        config1.a_map.put("key", "value");
//        config1.ignored_field = "This field will not be ignored";
//
//        // save
//        manager.save();
//
//        // reload
//        manager.load();
//        ExampleConfig config2 = manager.get();
//        System.out.println(config2.a_boolean);
//        System.out.println(config2.an_int);
//        System.out.println(config2.a_string);
//        System.out.println(config2.a_double);
//        System.out.println(config2.a_list);
//        System.out.println(config2.a_map);
//        System.out.println(config2.ignored_field);
//        System.out.println(config2.b_string);
//        System.out.println(config2.a_null);
//
//        manager.save();
    }
}
