package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class UrlBookReader implements BookReader{
    private final URL url;

    public UrlBookReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Book> readAll() {
        try {
            return loadFrom(url);
        } catch (IOException e) {
            return List.of();
        }
    }

    private List<Book> loadFrom(URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return BookReader.loadFrom(inputStream);
        }
    }

}