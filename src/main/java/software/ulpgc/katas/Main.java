package software.ulpgc.katas;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    static void main() throws IOException {
        File file = new File("document.iso8859-1.txt");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.length());
        System.out.println(file.canRead());
        try (InputStream is = new FileInputStream(file)) {
            byte[] bytes = is.readAllBytes();
            System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));
        }
    }
}
