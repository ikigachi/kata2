package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Title;
import software.ulpgc.katas.model.Genre;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public interface TitleReader {
    List<Title> readAll() throws IOException;

    static List<Title> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    static List<Title> loadFrom(Reader reader) throws IOException {
        return loadFrom(new BufferedReader(reader).lines().toList());
    }

    static List<Title> loadFrom(List<String> lines) {
        List<Title> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                list.add(toTitle(lines.get(i)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fila " + (i + 1) + " invalida: " + e.getMessage());
            }
        }
        return list;
    }

    static Title toTitle(String line) {
        return toTitle(line.split("\t", -1));
    }

    static Title toTitle(String[] fields) {
        if (fields.length < 2) throw new IllegalArgumentException("Faltan columnas");
        for (int i = 0; i < fields.length; i++) fields[i] = fields[i].trim();
        if (fields[0].isEmpty() || fields[1].isEmpty())
            throw new IllegalArgumentException("Falta titulo o genero");
        return new Title(fields[0], Genre.valueOf(fields[1]));
    }
}
