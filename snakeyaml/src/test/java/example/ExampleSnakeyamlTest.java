package example;

import io.github.blockneko11.nextconfig.serializer.SnakeyamlConfigSerializer;
import org.junit.jupiter.api.Test;

public class ExampleSnakeyamlTest {
    @Test
    public void file() throws Exception {
        Example.file(SnakeyamlConfigSerializer.DEFAULT, "yml");
    }
}
