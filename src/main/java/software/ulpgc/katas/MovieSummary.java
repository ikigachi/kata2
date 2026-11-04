package software.ulpgc.katas;

import software.ulpgc.katas.model.Title;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MovieSummary {
    public static Map<String, Integer> calculate(List<Title> items) {
        Map<String, Integer> summary = new TreeMap<>();
        for (Title item : items) {
            summary.merge(item.genre().name(), 1, Integer::sum);
        }
        return summary;
    }
}
