package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Title;
import java.io.IOException;
import java.util.List;

public interface TitleReader {
    List<Title> readAll() throws IOException;
}
