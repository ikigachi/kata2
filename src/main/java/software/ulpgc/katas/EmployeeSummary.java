package software.ulpgc.katas;

import software.ulpgc.katas.model.Employee;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EmployeeSummary {
    public static Map<String, Integer> calculate(List<Employee> items) {
        Map<String, Integer> summary = new TreeMap<>();
        for (Employee item : items) {
            summary.merge(item.gender().name(), 1, Integer::sum);
        }
        return summary;
    }
}
