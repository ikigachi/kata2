package software.ulpgc.katas;

import software.ulpgc.katas.model.Book;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BookSummary {
    public static Map<String, Integer> calculate(List<Book> items) {
        Map<String, Integer> summary = new TreeMap<>();
        for (Book item : items) {
            summary.merge(item.author(), 1, Integer::sum);
        }
        return summary;
    }
}
