package example;

import io.github.blockneko11.nextconfig.Config;
import io.github.blockneko11.nextconfig.annotation.PropertyIgnored;
import io.github.blockneko11.nextconfig.annotation.PropertyName;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ExampleConfig implements Config {
    public boolean a_boolean = true;

    public int an_int = 42;

    public String a_string = "This is a string!";

    public double a_double = 3.14;

    public List<String> a_list = Arrays.asList("This", "is", "a", "list");

    public Map<String, String> a_map;

    @PropertyIgnored
    public String ignored_field = "This field will be ignored";

    @PropertyName("str_b")
    public String b_string = "This string will be serialized as 'str_b'";

    public String a_null = null;

    public ExampleConfig() {
        this.a_map = new LinkedHashMap<>();
        this.a_map.put("key1", "value1");
        this.a_map.put("key2", "value2");
        this.a_map.put("key3", "value3");
    }
}
