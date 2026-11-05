package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Employee;
import java.io.IOException;
import java.util.List;

public interface EmployeeReader {
    List<Employee> readAll() throws IOException;
}
