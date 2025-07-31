package example;

import io.github.blockneko11.nextconfig.serializer.SnakeyamlConfigSerializer;
import io.github.blockneko11.nextconfig.throwable.ConfigException;
import org.junit.jupiter.api.Test;

public class ExampleSnakeyamlTest {
    @Test
    public void file() throws ConfigException {
        Example.file(SnakeyamlConfigSerializer.DEFAULT, "yml");
    }
}
