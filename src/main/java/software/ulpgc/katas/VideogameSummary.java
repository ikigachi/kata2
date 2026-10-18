package software.ulpgc.katas;

import software.ulpgc.katas.model.Videogame;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VideogameSummary {
    public static Map<String, Integer> calculate(List<Videogame> items) {
        Map<String, Integer> summary = new TreeMap<>();
        for (Videogame item : items) {
            summary.merge(item.platform(), 1, Integer::sum);
        }
        return summary;
    }
}
