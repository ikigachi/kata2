package software.ulpgc.katas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import software.ulpgc.katas.io.*;
import software.ulpgc.katas.model.Genre;
import software.ulpgc.katas.model.Title;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    @TempDir Path directory;

    private List<Title> parse(String text) throws IOException {
        return TsvTitleParser.parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rejectsInvalidTitleNames(String name) {
        assertThrows(IllegalArgumentException.class, () -> new Title(name, Genre.Action));
    }

    @Test
    void rejectsMissingGenre() {
        assertThrows(IllegalArgumentException.class, () -> new Title("Inception", null));
    }

    @Test
    void parsesUtf8AndNormalizesExternalWhitespace() throws IOException {
        assertEquals(List.of(new Title("Amélie", Genre.Animation)),
                parse("title\tgenre\n  Amélie  \t Animation \n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "title\tgenre\n"})
    void emptyDatasetsProduceEmptySummaries(String input) throws IOException {
        assertEquals(Map.of(), MovieSummary.calculate(parse(input)));
    }

    @Test
    void rejectsUnexpectedHeaders() {
        assertThrows(IllegalArgumentException.class, () -> parse("name\tgenre\n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Inception", "Inception\tAction\textra", "\tAction", "Inception\t", "Inception\tUnknown"})
    void invalidRowsIncludeTheirNumber(String row) {
        var error = assertThrows(IllegalArgumentException.class,
                () -> parse("title\tgenre\n" + row + "\n"));
        assertTrue(error.getMessage().startsWith("Invalid row 2: "));
    }

    @Test
    void fileUrlAndMemoryReadersProduceTheSameSummary() throws IOException {
        String data = Files.readString(Path.of("data.tsv"));
        Path file = directory.resolve("movies.tsv");
        Files.writeString(file, data);
        List<TitleReader> readers = List.of(new FileTitleReader(file.toString()),
                new UrlTitleReader(file.toUri().toURL()), () -> parse(data));
        for (TitleReader reader : readers) {
            assertEquals(Map.of("Action", 2, "Animation", 2, "ScienceFiction", 2),
                    MovieSummary.calculate(reader.readAll()));
        }
    }

    @Test
    void summaryDoesNotDependOnRowOrder() throws IOException {
        var titles = new ArrayList<>(parse("title\tgenre\nInception\tScienceFiction\nGladiator\tAction\n"));
        var expected = MovieSummary.calculate(titles);
        Collections.reverse(titles);
        assertEquals(expected, MovieSummary.calculate(titles));
    }

    @Test
    void missingResourcesRemainIoErrors() throws IOException {
        Path missing = directory.resolve("missing.tsv");
        assertThrows(IOException.class, () -> new FileTitleReader(missing.toString()).readAll());
        assertThrows(IOException.class, () -> new UrlTitleReader(missing.toUri().toURL()).readAll());
    }

    @Test
    void readFailuresRemainIoErrors() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Read failed");
            }
        };
        assertThrows(IOException.class, () -> TsvTitleParser.parse(broken));
    }

    @Test
    void parserAndWriterLeaveBorrowedStreamsOpen() throws IOException {
        var input = new ByteArrayInputStream("title\tgenre\n".getBytes(StandardCharsets.UTF_8)) {
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
        assertEquals(List.of(), TsvTitleParser.parse(input));
        var summary = MovieSummary.calculate(new FileTitleReader("data.tsv").readAll());
        SummaryWriter.write(summary, output);
        String expected = "Action: 2\nAnimation: 2\nScienceFiction: 2\n";
        assertEquals(expected, output.toString(StandardCharsets.UTF_8));
        Path file = directory.resolve("summary.txt");
        try (OutputStream stream = Files.newOutputStream(file)) {
            SummaryWriter.write(summary, stream);
        }
        assertEquals(expected, Files.readString(file));
    }

    @Test
    void invalidInputPreservesExistingOutput() throws Exception {
        Path summary = directory.resolve("summary.txt");
        Files.writeString(summary, "previous summary");
        Files.writeString(directory.resolve("data.tsv"), "title\tgenre\ninvalid\n");
        String executable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process process = new ProcessBuilder(executable, "-cp",
                Path.of("target/classes").toAbsolutePath().toString(), Main.class.getName())
                .directory(directory.toFile()).redirectErrorStream(true).start();
        String message = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(0, process.waitFor());
        assertTrue(message.contains("Invalid input: Invalid row 2:"));
        assertEquals("previous summary", Files.readString(summary));
    }
}
