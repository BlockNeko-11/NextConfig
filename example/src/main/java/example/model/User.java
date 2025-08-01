package example.model;

import io.github.blockneko11.nextconfig.entry.mapper.EntryMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String name;

    @Override
    public String toString() {
        return "User[" + this.name + "]";
    }

    public static final class UserMapper implements EntryMapper<User> {
        @Override
        public User toEntry(@NotNull Object property) {
            Map<String, Object> map = (Map<String, Object>) property;
            User user = new User();
            user.name = (String) map.get("name");
            return user;
        }

        @NotNull
        @Override
        public Object toProperty(User entry) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", entry.name);
            return map;
        }
    }
}
