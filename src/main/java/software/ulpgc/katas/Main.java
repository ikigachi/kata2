package software.ulpgc.katas;

import software.ulpgc.katas.io.*;
import java.io.*;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        String source = args.length == 0 ? "data.tsv" : args[0];
        try {
            EmployeeReader reader = source.contains("://")
                    ? new UrlEmployeeReader(URI.create(source).toURL())
                    : new FileEmployeeReader(source);
            var summary = EmployeeSummary.calculate(reader.readAll());
            SummaryWriter.write(summary, System.out);
            try (OutputStream output = new FileOutputStream("summary.txt")) {
                SummaryWriter.write(summary, output);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al leer o escribir datos: " + e.getMessage());
        }
    }
}
