package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public interface VideogameReader {
    List<Videogame> readAll();

    static List<Videogame> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(new InputStreamReader(inputStream));
    }

    static List<Videogame> loadFrom(Reader reader) throws IOException {
        return loadFrom(reader.readAllLines());
    }

    static List<Videogame> loadFrom(List<String> lines) {
        List<Videogame> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            Videogame videogame = toVideogame(lines.get(i));
            list.add(videogame);
        }
        return list;
    }

    static Videogame toVideogame(String line) {
        return toVideogame(line.split(",", -1));
    }

    static Videogame toVideogame(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                fields[i] = null;
            }
        }

        return new Videogame(
                fields[0],
                fields[1],
                fields[2],
                fields[3],
                fields[4],
                fields[5],
                fields[6],
                fields[7],
                fields[8],
                fields[9],
                fields[10]
        );
    }

}
