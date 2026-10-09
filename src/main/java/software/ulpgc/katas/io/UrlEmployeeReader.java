package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import software.ulpgc.katas.model.Gender;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlEmployeeReader implements EmployeeReader{
    private final URL url;

    public UrlEmployeeReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Employee> readAll() {
        try {
            return loadFrom(url);
        } catch (IOException e) {
            return List.of();
        }
    }

    private List<Employee> loadFrom(URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return loadFrom(inputStream);
        }
    }

    private List<Employee> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream));
    }

    private List<Employee> loadFrom(Reader reader) throws IOException {
        return loadFrom(reader.readAllLines());
    }

    private List<Employee> loadFrom(List<String> lines) {
        List<Employee> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            list.add(toEmployee(lines.get(i)));
        }
        return list;
    }

    private Employee toEmployee(String line) {
        return toEmployee(line.split("\t"));
    }

    private Employee toEmployee(String[] fields) {
        return new Employee(fields[0], toGender(fields[5]));
    }

    private Gender toGender(String field) {
        if (field.isEmpty()) return null;
        return Gender.valueOf(field);
    }
}
