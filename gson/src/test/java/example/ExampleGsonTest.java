package example;

import io.github.blockneko11.nextconfig.serializer.GsonConfigSerializer;
import io.github.blockneko11.nextconfig.throwable.ConfigException;
import org.junit.jupiter.api.Test;

public class ExampleGsonTest {
    @Test
    public void file() throws ConfigException {
        Example.file(GsonConfigSerializer.DEFAULT, "json");
    }
}
