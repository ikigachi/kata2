package software.ulpgc.katas;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    static void main() {
        String s = "¡Viva España!";
        byte[] utf = s.getBytes();
        byte[] iso = s.getBytes(StandardCharsets.ISO_8859_1);

        System.out.println("DECODING IN UTF-8 AND CODING IN UTF-8");
        System.out.println(new String(utf));
        System.out.println("DECODING IN UTF-8 AND CODING IN ISO-8859-1");
        System.out.println(new String(utf, StandardCharsets.ISO_8859_1));
        System.out.println("DECODING IN ISO-8859-1 AND CODING IN UTF-8");
        System.out.println(new String(iso));
        System.out.println("DECODING IN ISO-8859-1 AND CODING IN ISO-8859-1");
        System.out.println(new String(iso, StandardCharsets.ISO_8859_1));
    }
}
