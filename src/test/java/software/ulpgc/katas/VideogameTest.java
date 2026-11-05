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

class VideogameTest {
    private static final String HEADER = "rank,name,platform,year,genre,publisher,na,eu,jp,other,global";
    private static final String EXPECTED = "GEN: 1\nNES: 2\n";
    @TempDir Path directory;

    private List<Videogame> parse(String text) throws IOException {
        return CsvVideogameParser.parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void rejectsInvalidDomainFields(String value) {
        assertThrows(IllegalArgumentException.class, () -> new Videogame("1", value, "PC", "2020", "Action", "Publisher", "1", "1", "1", "1", "4"));
        assertThrows(IllegalArgumentException.class, () -> new Videogame("1", "A game", value, "2020", "Action", "Publisher", "1", "1", "1", "1", "4"));
    }

    @Test
    void parsesUtf8AndNormalizesExternalWhitespace() throws IOException {
        assertEquals(List.of(new Videogame("1", "PokÃ©mon", "PC", "2020", "Action", "Publisher", "1", "1", "1", "1", "4")), parse(HEADER + "\n" + "1,  PokÃ©mon  ,  PC  ,2020,Action,Publisher,1,1,1,1,4" + "\n"));
    }

    @Test
    void emptyDatasetsProduceEmptySummaries() throws IOException {
        assertEquals(Map.of(), VideogameSummary.calculate(parse("")));
        assertEquals(Map.of(), VideogameSummary.calculate(parse(HEADER + "\n")));
    }

    @Test
    void rejectsWrongOrMissingHeaders() {
        assertThrows(IllegalArgumentException.class, () -> parse("wrong header\n"));
        assertThrows(IllegalArgumentException.class, () -> parse("1,  PokÃ©mon  ,  PC  ,2020,Action,Publisher,1,1,1,1,4" + "\n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bad", "1,,PC,2020,Action,Publisher,1,1,1,1,4", "1,A game,,2020,Action,Publisher,1,1,1,1,4", "1,A game,PC,2020,Action,Publisher,1,1,1,1,4,extra", "1,\"Unclosed,PC,2020,Action,Publisher,1,1,1,1,4", "1,A\"game,PC,2020,Action,Publisher,1,1,1,1,4", "1,\"A game\"extra,PC,2020,Action,Publisher,1,1,1,1,4"})
    void invalidRowsIncludeTheirNumber(String row) {
        var error = assertThrows(IllegalArgumentException.class, () -> parse(HEADER + "\n" + row + "\n"));
        assertTrue(error.getMessage().startsWith("Invalid row 2: "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\n", "\r\n", "\r"})
    void acceptsStandardLineEndings(String newline) throws IOException {
        assertEquals(List.of(new Videogame("1", "PokÃ©mon", "PC", "2020", "Action", "Publisher", "1", "1", "1", "1", "4")), parse(HEADER + newline + "1,  PokÃ©mon  ,  PC  ,2020,Action,Publisher,1,1,1,1,4" + newline));
    }

    @Test
    void fileUrlAndMemoryReadersProduceTheSameSummary() throws IOException {
        String data = Files.readString(Path.of("data.csv"));
        Path file = directory.resolve("data.csv");
        Files.writeString(file, data);
        List<VideogameReader> readers = List.of(new FileVideogameReader(file.toString()),
                new UrlVideogameReader(file.toUri().toURL()), () -> parse(data));
        for (VideogameReader reader : readers) {
            var output = new ByteArrayOutputStream();
            SummaryWriter.write(VideogameSummary.calculate(reader.readAll()), output);
            assertEquals(EXPECTED, output.toString(StandardCharsets.UTF_8));
        }
    }

    @Test
    void summaryDoesNotDependOnRowOrder() throws IOException {
        var items = new ArrayList<>(new FileVideogameReader("data.csv").readAll());
        var expected = VideogameSummary.calculate(items);
        Collections.reverse(items);
        assertEquals(expected, VideogameSummary.calculate(items));
    }

    @Test
    void missingResourcesRemainIoErrors() throws IOException {
        Path missing = directory.resolve("missing-data");
        assertThrows(IOException.class, () -> new FileVideogameReader(missing.toString()).readAll());
        assertThrows(IOException.class, () -> new UrlVideogameReader(missing.toUri().toURL()).readAll());
    }

    @Test
    void readFailuresRemainIoErrors() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Read failed");
            }
        };
        assertThrows(IOException.class, () -> CsvVideogameParser.parse(broken));
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
        assertEquals(List.of(), CsvVideogameParser.parse(input));
        var summary = VideogameSummary.calculate(new FileVideogameReader("data.csv").readAll());
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
        Files.writeString(directory.resolve("data.csv"), HEADER + "\n" + "1,A game,PC,2020,Action,Publisher,1,1,1,1,4" + "\ninvalid\n");
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
        var items = parse(HEADER + "\n1,\"Hello, \"\"World\"\"\",\"PC, Console\",2020,Action,Publisher,1,1,1,1,4\n");
        assertEquals("Hello, \"World\"", items.getFirst().name());
        assertEquals(Map.of("PC, Console", 1), VideogameSummary.calculate(items));
    }

    @Test
    void quotedFieldsCanContainLineBreaks() throws IOException {
        var items = parse(HEADER + "\n1,\"First\nSecond\",PC,2020,Action,Publisher,1,1,1,1,4\n");
        assertEquals("First\nSecond", items.getFirst().name());
        assertEquals("PC", items.getFirst().platform());
    }

}
