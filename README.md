# Kata 2

## Objective

Read tabular data through an InputStream, transform each row into a domain object, count videogames by platform, and write the summary through an OutputStream. Reading, parsing, calculation, and writing have separate responsibilities.

## Compilation and execution

Run from the project root with JDK 27:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main"
```

The program reads data.csv and writes the following summary to the console and summary.txt:

```text
GEN: 1
NES: 2
```

Pass another file path or a URL as the first argument:

```bash
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main" -Dexec.args="videogames.csv"
```

The UTF-8 input must include the header columns `rank, name, platform, year, genre, publisher, na, eu, jp, other, global` in that order, separated by commas, and exactly the same number of fields in each record. Name and platform must not be blank. Other columns are kept as text; no numeric analysis is performed. Surrounding field whitespace is removed. Empty and header-only files produce an empty summary.

CSV fields containing commas, line breaks, or quotes must be quoted. A quote inside a quoted field is escaped as two quotes. Malformed quoting is rejected. Error row numbers refer to logical CSV records, including the header.

Invalid rows report their row number. Input errors are reported separately from reading or writing failures. Input is fully validated before output begins, so invalid data leaves an existing summary.txt unchanged.

## Dependencies and JDK

- JDK 27
- Maven
- JUnit Jupiter 5.11.4, used only for tests

The application uses the Java standard library and has no external runtime dependencies.

## Project Structure

Base package: software.ulpgc.katas.

- Main.java: selects the input adapter and connects reading, calculation, and output.
- model/Videogame.java: stores a videogame's name, platform, ranking, release year, genre, publisher, and sales and validates the required domain fields.
- io/VideogameReader.java: small loading contract shared by input adapters.
- io/FileVideogameReader.java: opens and closes file input streams.
- io/UrlVideogameReader.java: opens and closes URL input streams.
- io/CsvVideogameParser.java: validates the input format and converts rows into domain objects.
- io/CsvRowReader.java: internal CSV decoding shared by the row parser.
- VideogameSummary.java: counts videogames by platform without knowing the input source or output destination.
- io/SummaryWriter.java: writes the calculated summary in UTF-8 to the supplied OutputStream.
- data.csv: small dataset for the default execution.
- src/test/java/software/ulpgc/katas/VideogameTest.java: automated verification.

The parser and writer leave caller-supplied streams open. Input adapters close streams they open; Main closes its output file and leaves System.out open.

## Repetitions

The kata was repeated in separate branches:

- `master`: employees grouped by gender.
- `book-version`: books grouped by author.
- `videogame-version`: videogames grouped by platform.
- `movie-version`: movies grouped by genre.

Each version keeps the same reader, entity, summary, and writer structure. The variations are the domain, dataset format, and grouping column.

## Verification

Run the automated tests:

```bash
mvn test
```

The 28 test cases check domain validation, UTF-8 and whitespace handling, headers, malformed rows, empty input, standard line endings, missing resources, reading and writing failures, unchanged summaries after reordering, matching file, URL, and memory inputs, and matching console, file, and memory outputs. They also check that borrowed streams stay open and invalid input preserves an existing summary file. CSV tests include quoted commas, escaped quotes, and multiline fields.

The URL adapter is tested with a local file URL, so tests do not need an Internet connection.

## Video

Pending: add the explanatory video link (maximum duration: 7 minutes).
