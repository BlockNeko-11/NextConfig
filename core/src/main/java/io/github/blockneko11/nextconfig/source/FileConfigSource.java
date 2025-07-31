package io.github.blockneko11.nextconfig.source;

import io.github.blockneko11.nextconfig.throwable.ConfigIOException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConfigSource implements ConfigSource {
    private final Path file;

    public FileConfigSource(File file) {
        this(file.toPath());
    }

    public FileConfigSource(Path path) {
        this.file = path;
    }

    public FileConfigSource(String path) {
        this(Paths.get(path));
    }

    @NotNull
    @Override
    public String read() throws ConfigIOException {
        try {
            if (!Files.exists(this.file)) {
                Files.createFile(this.file);
            }

            BufferedReader br = Files.newBufferedReader(this.file, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new ConfigIOException(e);
        }
    }

    @Override
    public void write(@NotNull String text) throws ConfigIOException {
        try {
            Files.createDirectories(this.file.getParent());

            BufferedWriter bw = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8);
            bw.write(text);
            bw.flush();

            bw.close();
        } catch (IOException e) {
            throw new ConfigIOException(e);
        }
    }
}
