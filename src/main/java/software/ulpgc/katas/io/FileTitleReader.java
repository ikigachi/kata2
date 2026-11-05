package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Title;
import java.io.*;
import java.util.List;

public class FileTitleReader implements TitleReader {
    private final String path;

    public FileTitleReader(String path) {
        this.path = path;
    }

    @Override
    public List<Title> readAll() throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            return TsvTitleParser.parse(inputStream);
        }
    }
}
