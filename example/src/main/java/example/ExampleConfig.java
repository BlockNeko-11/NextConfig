package example;

import example.model.User;
import io.github.blockneko11.nextconfig.annotation.Config;
import io.github.blockneko11.nextconfig.annotation.entry.Ignored;
import io.github.blockneko11.nextconfig.annotation.entry.Mapper;
import io.github.blockneko11.nextconfig.annotation.entry.Name;

import java.util.*;

@Config
public final class ExampleConfig {
    public boolean a_boolean = true;

    public int an_int = 42;

    public String a_string = "This is a string!";

    public double a_double = 3.14;

    public List<String> a_list = Arrays.asList("This", "is", "a", "list");

    public Map<String, String> a_map;

    @Ignored
    public String ignored_field = "This field will be ignored";

    @Name("str_b")
    public String b_string = "This string will be serialized as 'str_b'";

    public String a_null = null;

    public State a_enum = State.ON;

    public enum State {
        ON, OFF
    }

    public Integer a_wrapped_int = 1;

    public Optional<String> a_optional = Optional.of("This is a Optional instance");

    @Mapper(User.UserMapper.class)
    public User a_user = new User();

    public ExampleConfig() {
        this.a_map = new LinkedHashMap<>();
        this.a_map.put("key1", "value1");
        this.a_map.put("key2", "value2");
        this.a_map.put("key3", "value3");

        this.a_user.setName("BlockNeko11");
    }
}
