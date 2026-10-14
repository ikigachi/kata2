package software.ulpgc.katas.io;


import software.ulpgc.katas.model.Book;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public interface BookReader {
    List<Book> readAll();

    static List<Book> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream));
    }

    static List<Book> loadFrom(Reader reader) throws IOException {
        return loadFrom(reader.readAllLines());
    }

    static List<Book> loadFrom(List<String> lines) {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            Book videogame = toBook(lines.get(i));
            list.add(videogame);
        }
        return list;
    }

    static Book toBook(String line) {
        return toBook(line.split(",", -1));
    }

    static Book toBook(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                fields[i] = null;
            }
        }

        return new Book();
    }

}