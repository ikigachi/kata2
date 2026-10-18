package software.ulpgc.katas.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SummaryWriter {
    public static void write(Map<String, Integer> summary, OutputStream output) throws IOException {
        Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        for (var entry : summary.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        writer.flush();
    }
}
