package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import java.io.*;
import java.util.List;

public class FileEmployeeReader implements EmployeeReader {
    private final String path;

    public FileEmployeeReader(String path) {
        this.path = path;
    }

    @Override
    public List<Employee> readAll() throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            return EmployeeReader.loadFrom(inputStream);
        }
    }
}
