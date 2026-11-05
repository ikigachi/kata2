package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import software.ulpgc.katas.model.Gender;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TsvEmployeeParser {
    public static List<Employee> parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        String header = reader.readLine();
        if (header == null) return List.of();
        if (!header.equals("name\tlastName\tage\tdepartment\tsalary\tgender"))
            throw new IllegalArgumentException("Expected TSV header: name, lastName, age, department, salary, gender");
        List<Employee> employees = new ArrayList<>();
        String line;
        int row = 1;
        while ((line = reader.readLine()) != null) {
            row++;
            try {
                employees.add(parseEmployee(line));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid row " + row + ": " + e.getMessage(), e);
            }
        }
        return List.copyOf(employees);
    }

    private static Employee parseEmployee(String line) {
        String[] fields = line.split("\t", -1);
        if (fields.length != 6) throw new IllegalArgumentException("Expected six columns");
        String name = fields[0].strip();
        String gender = fields[5].strip();
        Gender parsedGender;
        try {
            parsedGender = Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown gender '" + gender
                    + "'; expected " + Arrays.toString(Gender.values()), e);
        }
        return new Employee(name, parsedGender);
    }
}
