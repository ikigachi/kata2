package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
            return EmployeeReader.loadFrom(inputStream);
        }
    }

}
