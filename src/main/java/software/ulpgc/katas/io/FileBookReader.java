package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;
import java.io.*;
import java.util.List;

public class FileBookReader implements BookReader {
    private final String path;

    public FileBookReader(String path) {
        this.path = path;
    }

    @Override
    public List<Book> readAll() throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            return BookReader.loadFrom(inputStream);
        }
    }
}
