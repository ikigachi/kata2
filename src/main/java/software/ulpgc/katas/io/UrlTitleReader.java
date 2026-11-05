package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Title;
import java.io.*;
import java.net.URL;
import java.util.List;

public class UrlTitleReader implements TitleReader {
    private final URL url;

    public UrlTitleReader(URL url) {
        this.url = url;
    }

    @Override
    public List<Title> readAll() throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return TsvTitleParser.parse(inputStream);
        }
    }
}
