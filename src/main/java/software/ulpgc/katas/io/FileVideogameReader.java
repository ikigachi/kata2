package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;
import java.io.*;
import java.util.List;

public class FileVideogameReader implements VideogameReader {
    private final String path;

    public FileVideogameReader(String path) {
        this.path = path;
    }

    @Override
    public List<Videogame> readAll() throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            return CsvVideogameParser.parse(inputStream);
        }
    }
}
