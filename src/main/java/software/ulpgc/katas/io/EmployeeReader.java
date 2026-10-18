package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import software.ulpgc.katas.model.Gender;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeReader {
    List<Employee> readAll() throws IOException;

    static List<Employee> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    static List<Employee> loadFrom(Reader reader) throws IOException {
        return loadFrom(new BufferedReader(reader).lines().toList());
    }

    static List<Employee> loadFrom(List<String> lines) {
        List<Employee> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                list.add(toEmployee(lines.get(i)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fila " + (i + 1) + " invalida: " + e.getMessage());
            }
        }
        return list;
    }

    static Employee toEmployee(String line) {
        return toEmployee(line.split("\t", -1));
    }

    static Employee toEmployee(String[] fields) {
        if (fields.length < 6) throw new IllegalArgumentException("Faltan columnas");
        for (int i = 0; i < fields.length; i++) fields[i] = fields[i].trim();
        if (fields[0].isEmpty() || fields[5].isEmpty())
            throw new IllegalArgumentException("Falta nombre o genero");
        return new Employee(fields[0], Gender.valueOf(fields[5]));
    }
}
