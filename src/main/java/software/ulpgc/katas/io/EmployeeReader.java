package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import software.ulpgc.katas.model.Gender;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeReader {
    List<Employee> readAll();

    static List<Employee> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream));
    }

    static List<Employee> loadFrom(Reader reader) throws IOException {
        return loadFrom(reader.readAllLines());
    }

    static List<Employee> loadFrom(List<String> lines) {
        List<Employee> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            Employee employee = toEmployee(lines.get(i));
            list.add(employee);
        }
        return list;
    }

    static Employee toEmployee(String line) {
        return toEmployee(line.split("\t"));
    }

    static Employee toEmployee(String[] fields) {
        return new Employee(fields[0], toGender(fields[5]));
    }

    static Gender toGender(String field) {
        return field.isEmpty() ? null : Gender.valueOf(field);
    }

}
