package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Videogame;
import java.io.IOException;
import java.util.List;

public interface VideogameReader {
    List<Videogame> readAll() throws IOException;
}
