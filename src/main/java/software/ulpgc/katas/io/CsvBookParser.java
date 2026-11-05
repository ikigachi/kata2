package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvBookParser {
    public static List<Book> parse(InputStream input) throws IOException {
        CsvRowReader reader = new CsvRowReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        List<String> header = reader.readRow();
        if (header == null) return List.of();
        if (!header.equals(List.of("author", "title")))
            throw new IllegalArgumentException("Expected CSV header: author,title");
        List<Book> items = new ArrayList<>();
        int row = 2;
        while (true) {
            try {
                List<String> fields = reader.readRow();
                if (fields == null) break;
                if (fields.size() != 2) throw new IllegalArgumentException("Expected 2 columns");
                items.add(new Book(fields.get(0).strip(), fields.get(1).strip()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid row " + row + ": " + e.getMessage(), e);
            }
            row++;
        }
        return List.copyOf(items);
    }
}
