package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Genre;
import software.ulpgc.katas.model.Title;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TsvTitleParser {
    public static List<Title> parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        String header = reader.readLine();
        if (header == null) return List.of();
        if (!header.equals("title\tgenre"))
            throw new IllegalArgumentException("Expected TSV header: title<TAB>genre");
        List<Title> titles = new ArrayList<>();
        String line;
        int row = 1;
        while ((line = reader.readLine()) != null) {
            row++;
            try {
                titles.add(parseTitle(line));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid row " + row + ": " + e.getMessage(), e);
            }
        }
        return List.copyOf(titles);
    }

    private static Title parseTitle(String line) {
        String[] fields = line.split("\t", -1);
        if (fields.length != 2) throw new IllegalArgumentException("Expected title and genre columns");
        String name = fields[0].strip();
        String genre = fields[1].strip();
        Genre parsedGenre;
        try {
            parsedGenre = Genre.valueOf(genre);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown genre '" + genre
                    + "'; expected " + Arrays.toString(Genre.values()), e);
        }
        return new Title(name, parsedGenre);
    }
}
