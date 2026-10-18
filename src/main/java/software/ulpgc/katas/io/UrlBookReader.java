package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;
import java.io.*;
import java.net.URL;
import java.util.List;

public class UrlBookReader implements BookReader {
    private final URL url;

    public UrlBookReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Book> readAll() throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return BookReader.loadFrom(inputStream);
        }
    }
}
