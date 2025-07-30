package io.github.blockneko11.nextconfig.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public final class PropertiesSerializer implements ConfigSerializer {
    @Nullable
    @Override
    public Map<String, Object> parse(String text) {
        try (StringReader sr = new StringReader(text)) {
            Properties prop = new Properties();
            prop.load(sr);
            return prop.stringPropertyNames()
                    .stream()
                    .collect(Collectors.toMap(k -> k, prop::getProperty));
        } catch (IOException e) {
            return null;
        }
    }

    @NotNull
    @Override
    public String serialize(Map<String, Object> conf) {
        try (StringWriter sw = new StringWriter()) {
            Properties prop = new Properties();
            conf.forEach((key, value) -> prop.setProperty(key, value.toString()));
            prop.store(sw, null);
            return sw.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
