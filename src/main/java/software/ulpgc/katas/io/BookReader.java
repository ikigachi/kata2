package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public interface BookReader {
    List<Book> readAll() throws IOException;

    static List<Book> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    static List<Book> loadFrom(Reader reader) throws IOException {
        return loadFrom(new BufferedReader(reader).lines().toList());
    }

    static List<Book> loadFrom(List<String> lines) {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                list.add(toBook(lines.get(i)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fila " + (i + 1) + " invalida: " + e.getMessage());
            }
        }
        return list;
    }

    static Book toBook(String line) {
        return toBook(line.split(",", -1));
    }

    static Book toBook(String[] fields) {
        if (fields.length < 2) throw new IllegalArgumentException("Faltan columnas");
        for (int i = 0; i < fields.length; i++) fields[i] = fields[i].trim();
        if (fields[0].isEmpty() || fields[1].isEmpty())
            throw new IllegalArgumentException("Falta autor o titulo");
        return new Book(fields[0], fields[1]);
    }
}
