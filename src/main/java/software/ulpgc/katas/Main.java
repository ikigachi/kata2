package software.ulpgc.katas;

import software.ulpgc.katas.io.UrlBookReader;
import software.ulpgc.katas.io.BookReader;
import software.ulpgc.katas.model.Book;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {
    static void main() throws IOException {
        URL url = new URL("https://gist.githubusercontent.com/um-zagaa/210ea9ca512900df7ecde73730a4bc40/raw/9d7f3d6f8f741c99a1ce3bc5b7a0c33ca89f5b8a/author_books.csv");
        BookReader reader = new UrlBookReader(url);
        List<Book> books = reader.readAll();
        for (Book book : books) {
            System.out.println(book);
        }


    }
}