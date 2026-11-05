package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Book;
import java.io.IOException;
import java.util.List;

public interface BookReader {
    List<Book> readAll() throws IOException;
}
