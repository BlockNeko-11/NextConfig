package io.github.blockneko11.nextconfig.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

        StringBuilder sb = new StringBuilder();

        try (FileInputStream is = new FileInputStream(file);
             InputStreamReader sr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(sr)) {
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }

        return sb.toString();
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

        try (FileOutputStream os = new FileOutputStream(file);
             OutputStreamWriter sw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(sw)) {
            bw.write(text);
            bw.newLine();
            bw.flush();
        }
    }

    private FileUtils() {
    }
}
