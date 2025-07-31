package io.github.blockneko11.nextconfig.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {
    public static String read(String path) throws IOException {
        return read(new File(path));
    }

    public static String read(Path path) throws IOException {
        return read(path.toFile());
    }

    public static String read(File file) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
            return "";
        }

        return IOUtils.read(Files.newInputStream(file.toPath()));
    }

    public static void write(String path, String text) throws IOException {
        write(new File(path), text);
    }

    public static void write(Path path, String text) throws IOException {
        write(path.toFile(), text);
    }

    public static void write(File file, String text) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }

        IOUtils.write(Files.newOutputStream(file.toPath()), text);
    }

    private FileUtils() {
    }
}
