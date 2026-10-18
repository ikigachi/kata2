package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;
import java.io.*;
import java.net.URL;
import java.util.List;

public class UrlVideogameReader implements VideogameReader {
    private final URL url;

    public UrlVideogameReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Videogame> readAll() throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return VideogameReader.loadFrom(inputStream);
        }
    }
}
