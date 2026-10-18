package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public interface VideogameReader {
    List<Videogame> readAll() throws IOException;

    static List<Videogame> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    static List<Videogame> loadFrom(Reader reader) throws IOException {
        return loadFrom(new BufferedReader(reader).lines().toList());
    }

    static List<Videogame> loadFrom(List<String> lines) {
        List<Videogame> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                list.add(toVideogame(lines.get(i)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fila " + (i + 1) + " invalida: " + e.getMessage());
            }
        }
        return list;
    }

    static Videogame toVideogame(String line) {
        return toVideogame(line.split(",", -1));
    }

    static Videogame toVideogame(String[] fields) {
        if (fields.length < 11) throw new IllegalArgumentException("Faltan columnas");
        for (int i = 0; i < fields.length; i++) fields[i] = fields[i].trim();
        if (fields[1].isEmpty() || fields[2].isEmpty())
            throw new IllegalArgumentException("Falta nombre o plataforma");
        return new Videogame(fields[0], fields[1], fields[2], fields[3], fields[4],
                fields[5], fields[6], fields[7], fields[8], fields[9], fields[10]);
    }
}
