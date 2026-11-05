package software.ulpgc.katas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import software.ulpgc.katas.io.*;
import software.ulpgc.katas.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private static final String HEADER = "author,title";
    private static final String EXPECTED = "Becquer: 1\nCervantes: 2\n";
    @TempDir Path directory;

    private List<Book> parse(String text) throws IOException {
        return CsvBookParser.parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rejectsInvalidDomainFields(String value) {
        assertThrows(IllegalArgumentException.class, () -> new Book(value, "A book"));
        assertThrows(IllegalArgumentException.class, () -> new Book("An author", value));
    }

    @Test
    void parsesUtf8AndNormalizesExternalWhitespace() throws IOException {
        assertEquals(List.of(new Book("GarcÃ­a", "A book")), parse(HEADER + "\n" + "  GarcÃ­a  ,  A book  " + "\n"));
    }

    @Test
    void emptyDatasetsProduceEmptySummaries() throws IOException {
        assertEquals(Map.of(), BookSummary.calculate(parse("")));
        assertEquals(Map.of(), BookSummary.calculate(parse(HEADER + "\n")));
    }

    @Test
    void rejectsWrongOrMissingHeaders() {
        assertThrows(IllegalArgumentException.class, () -> parse("wrong header\n"));
        assertThrows(IllegalArgumentException.class, () -> parse("  GarcÃ­a  ,  A book  " + "\n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bad", ",A book", "An author,", "An author,A book,extra", "\"Unclosed,A book", "An\"author,A book", "\"An author\"extra,A book"})
    void invalidRowsIncludeTheirNumber(String row) {
        var error = assertThrows(IllegalArgumentException.class, () -> parse(HEADER + "\n" + row + "\n"));
        assertTrue(error.getMessage().startsWith("Invalid row 2: "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\n", "\r\n", "\r"})
    void acceptsStandardLineEndings(String newline) throws IOException {
        assertEquals(List.of(new Book("GarcÃ­a", "A book")), parse(HEADER + newline + "  GarcÃ­a  ,  A book  " + newline));
    }

    @Test
    void fileUrlAndMemoryReadersProduceTheSameSummary() throws IOException {
        String data = Files.readString(Path.of("data.csv"));
        Path file = directory.resolve("data.csv");
        Files.writeString(file, data);
        List<BookReader> readers = List.of(new FileBookReader(file.toString()),
                new UrlBookReader(file.toUri().toURL()), () -> parse(data));
        for (BookReader reader : readers) {
            var output = new ByteArrayOutputStream();
            SummaryWriter.write(BookSummary.calculate(reader.readAll()), output);
            assertEquals(EXPECTED, output.toString(StandardCharsets.UTF_8));
        }
    }

    @Test
    void summaryDoesNotDependOnRowOrder() throws IOException {
        var items = new ArrayList<>(new FileBookReader("data.csv").readAll());
        var expected = BookSummary.calculate(items);
        Collections.reverse(items);
        assertEquals(expected, BookSummary.calculate(items));
    }

    @Test
    void missingResourcesRemainIoErrors() throws IOException {
        Path missing = directory.resolve("missing-data");
        assertThrows(IOException.class, () -> new FileBookReader(missing.toString()).readAll());
        assertThrows(IOException.class, () -> new UrlBookReader(missing.toUri().toURL()).readAll());
    }

    @Test
    void readFailuresRemainIoErrors() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Read failed");
            }
        };
        assertThrows(IOException.class, () -> CsvBookParser.parse(broken));
    }

    @Test
    void writeFailuresRemainIoErrors() {
        OutputStream broken = new OutputStream() {
            @Override
            public void write(int value) throws IOException {
                throw new IOException("Write failed");
            }
        };
        assertThrows(IOException.class, () -> SummaryWriter.write(Map.of("A group", 1), broken));
    }

    @Test
    void parserAndWriterLeaveBorrowedStreamsOpen() throws IOException {
        var input = new ByteArrayInputStream((HEADER + "\n").getBytes(StandardCharsets.UTF_8)) {
            @Override
            public void close() {
                fail("Parser closed its borrowed stream");
            }
        };
        var output = new ByteArrayOutputStream() {
            @Override
            public void close() {
                fail("Writer closed its borrowed stream");
            }
        };
        assertEquals(List.of(), CsvBookParser.parse(input));
        var summary = BookSummary.calculate(new FileBookReader("data.csv").readAll());
        SummaryWriter.write(summary, output);
        assertEquals(EXPECTED, output.toString(StandardCharsets.UTF_8));
        Path file = directory.resolve("summary.txt");
        try (OutputStream stream = Files.newOutputStream(file)) {
            SummaryWriter.write(summary, stream);
        }
        assertEquals(EXPECTED, Files.readString(file));
    }

    private String runMain(String... args) throws Exception {
        String executable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        List<String> command = new ArrayList<>(List.of(executable, "-cp",
                Path.of("target/classes").toAbsolutePath().toString(), Main.class.getName()));
        command.addAll(List.of(args));
        Process process = new ProcessBuilder(command).directory(directory.toFile())
                .redirectErrorStream(true).start();
        String message = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(0, process.waitFor());
        return message;
    }

    @Test
    void mainWritesMatchingConsoleAndFileSummaries() throws Exception {
        Files.copy(Path.of("data.csv"), directory.resolve("data.csv"));
        assertEquals(EXPECTED, runMain());
        assertEquals(EXPECTED, Files.readString(directory.resolve("summary.txt")));
    }

    @Test
    void invalidInputPreservesExistingOutput() throws Exception {
        Path summary = directory.resolve("summary.txt");
        Files.writeString(summary, "previous summary");
        Files.writeString(directory.resolve("data.csv"), HEADER + "\n" + "An author,A book" + "\ninvalid\n");
        assertTrue(runMain().contains("Invalid input: Invalid row 3:"));
        assertEquals("previous summary", Files.readString(summary));
    }

    @Test
    void mainReportsMissingResourcesWithoutCreatingOutput() throws Exception {
        assertTrue(runMain("missing-data").contains("Unable to read or write data:"));
        assertFalse(Files.exists(directory.resolve("summary.txt")));
    }

    @Test
    void quotedCommasAndEscapedQuotesKeepTheirColumns() throws IOException {
        assertEquals(List.of(new Book("Doe, Jane", "A \"quoted\" title")),
                parse(HEADER + "\n\"Doe, Jane\",\"A \"\"quoted\"\" title\"\n"));
    }

    @Test
    void quotedFieldsCanContainLineBreaks() throws IOException {
        assertEquals(List.of(new Book("An author", "First\nSecond")),
                parse(HEADER + "\nAn author,\"First\nSecond\"\n"));
    }

}
