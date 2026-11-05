package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvVideogameParser {
    public static List<Videogame> parse(InputStream input) throws IOException {
        CsvRowReader reader = new CsvRowReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        List<String> header = reader.readRow();
        if (header == null) return List.of();
        if (!header.equals(List.of("rank", "name", "platform", "year", "genre", "publisher", "na", "eu", "jp", "other", "global")))
            throw new IllegalArgumentException("Expected CSV header: rank,name,platform,year,genre,publisher,na,eu,jp,other,global");
        List<Videogame> items = new ArrayList<>();
        int row = 2;
        while (true) {
            try {
                List<String> fields = reader.readRow();
                if (fields == null) break;
                if (fields.size() != 11) throw new IllegalArgumentException("Expected 11 columns");
                items.add(new Videogame(fields.get(0).strip(), fields.get(1).strip(), fields.get(2).strip(), fields.get(3).strip(), fields.get(4).strip(),
                        fields.get(5).strip(), fields.get(6).strip(), fields.get(7).strip(), fields.get(8).strip(), fields.get(9).strip(), fields.get(10).strip()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid row " + row + ": " + e.getMessage(), e);
            }
            row++;
        }
        return List.copyOf(items);
    }
}
