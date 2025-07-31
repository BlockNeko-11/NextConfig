package example;

import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.EntryIgnored;
import io.github.blockneko11.nextconfig.annotation.EntryName;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Config
public final class ExampleConfig {
    public boolean a_boolean = true;

    public int an_int = 42;

    public String a_string = "This is a string!";

    public double a_double = 3.14;

    public List<String> a_list = Arrays.asList("This", "is", "a", "list");

    public Map<String, String> a_map;

    @EntryIgnored
    public String ignored_field = "This field will be ignored";

    @EntryName("str_b")
    public String b_string = "This string will be serialized as 'str_b'";

    public String a_null = null;

    public ExampleConfig() {
        this.a_map = new LinkedHashMap<>();
        this.a_map.put("key1", "value1");
        this.a_map.put("key2", "value2");
        this.a_map.put("key3", "value3");
    }
}
