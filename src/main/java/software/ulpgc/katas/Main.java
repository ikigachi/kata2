package software.ulpgc.katas;

import software.ulpgc.katas.io.*;
import java.io.*;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        String source = args.length == 0 ? "data.csv" : args[0];
        try {
            VideogameReader reader = source.contains("://")
                    ? new UrlVideogameReader(URI.create(source).toURL())
                    : new FileVideogameReader(source);
            var summary = VideogameSummary.calculate(reader.readAll());
            SummaryWriter.write(summary, System.out);
            try (OutputStream output = new FileOutputStream("summary.txt")) {
                SummaryWriter.write(summary, output);
            }
        } catch (IOException e) {
            System.err.println("Unable to read or write data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}
