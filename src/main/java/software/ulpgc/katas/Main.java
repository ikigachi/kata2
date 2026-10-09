package software.ulpgc.katas;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    static void main() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/sachin365123/CSV-files-for-Data-Science-and-Machine-Learning/refs/heads/main/Employee%20Dataset%20-%20TSV.txt");
        File file = new File("employees.tsv");
        try (InputStream is = url.openStream(); OutputStream os = new FileOutputStream(file)) {
            os.write(is.readAllBytes());
        }

    }
}
