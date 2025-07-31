package io.github.blockneko11.nextconfig.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    public static String read(InputStream is) throws IOException {
        return read(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public static String read(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }

            return sb.toString();
        }
    }

    public static void write(OutputStream os, String text) throws IOException {
        write(new OutputStreamWriter(os, StandardCharsets.UTF_8), text);
    }

    public static void write(Writer writer, String text) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(text);
            bw.flush();
        }
    }

    private IOUtils() {
    }
}
