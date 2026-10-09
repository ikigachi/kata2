package software.ulpgc.katas;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    static void main() {
        File file = new File("document.iso8859-1.txt");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.length());
        System.out.println(file.canRead());
    }
}
