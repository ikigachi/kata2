package software.ulpgc.katas.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

class CsvRowReader {
    private final PushbackReader reader;

    CsvRowReader(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    List<String> readRow() throws IOException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        boolean afterQuote = false;
        boolean started = false;
        int character;
        while ((character = reader.read()) != -1) {
            started = true;
            if (quoted) {
                if (character == '"') {
                    quoted = false;
                    afterQuote = true;
                } else field.append((char) character);
            } else if (afterQuote && character == '"') {
                field.append('"');
                quoted = true;
                afterQuote = false;
            } else if (character == ',') {
                fields.add(field.toString());
                field.setLength(0);
                afterQuote = false;
            } else if (character == '\n' || character == '\r') {
                if (character == '\r') skipLineFeed();
                fields.add(field.toString());
                return fields;
            } else if (afterQuote) {
                throw new IllegalArgumentException("Unexpected character after closing quote");
            } else if (character == '"') {
                if (!field.isEmpty()) throw new IllegalArgumentException("Unexpected quote in unquoted field");
                quoted = true;
            } else field.append((char) character);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted field");
        if (!started) return null;
        fields.add(field.toString());
        return fields;
    }

    private void skipLineFeed() throws IOException {
        int next = reader.read();
        if (next != -1 && next != '\n') reader.unread(next);
    }
}
