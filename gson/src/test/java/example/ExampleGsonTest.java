package example;

import io.github.blockneko11.nextconfig.serializer.GsonConfigSerializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ExampleGsonTest {
    @Test
    public void file() throws IOException {
        Example.file(GsonConfigSerializer.DEFAULT, "json");
    }
}
