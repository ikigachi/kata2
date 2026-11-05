# Kata 2

## Objective

Read tabular data through an InputStream, transform each row into a domain object, count movies by genre, and write the summary through an OutputStream. Reading, TSV parsing, calculation, and writing have separate responsibilities.

## Compilation and execution

Run from the project root with JDK 27:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main"
```

The program reads data.tsv and writes the following summary to the console and summary.txt:

```text
Action: 2
Animation: 2
ScienceFiction: 2
```

Pass another file path or a URL as the first argument:

```bash
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main" -Dexec.args="movies.tsv"
```

The UTF-8 input must have the header `title<TAB>genre` and exactly two tab-separated columns per row. Names must not be blank. Supported genres are `ScienceFiction`, `Action`, and `Animation`, with matching capitalization. Surrounding field whitespace is removed. Empty files and header-only files produce an empty summary.

Malformed rows report their row number. Invalid input is reported separately from reading or writing failures. Input is fully validated before output begins, so invalid data leaves an existing summary.txt unchanged.

## Dependencies and JDK

- JDK 27
- Maven
- JUnit Jupiter 5.11.4, used only for tests

The application uses the Java standard library and has no external runtime dependencies.

## Project Structure

Base package: software.ulpgc.katas.

- Main.java: selects the input adapter and connects reading, calculation, and output.
- model/Title.java: immutable movie value that requires a nonblank name and a genre.
- model/Genre.java: supported movie genres.
- io/TitleReader.java: small loading contract shared by input adapters.
- io/FileTitleReader.java: opens and closes file input streams.
- io/UrlTitleReader.java: opens and closes URL input streams.
- io/TsvTitleParser.java: validates the TSV format and converts rows into titles.
- MovieSummary.java: counts titles by genre without knowing their input source or output destination.
- io/SummaryWriter.java: writes the sorted summary in UTF-8 to the supplied OutputStream.
- data.tsv: small movie dataset for the default execution.
- src/test/java/software/ulpgc/katas/MovieTest.java: automated verification.

The parser and writer leave caller-supplied streams open. Input adapters close the streams they open; Main closes its output file and leaves System.out open.

## Repetitions

The kata was repeated in separate branches:

- `master`: employees grouped by gender.
- `book-version`: books grouped by author.
- `videogame-version`: videogames grouped by platform.
- `movie-version`: movies grouped by genre, with separate TSV parsing and domain validation.

Each version keeps the same reader, entity, summary, and writer structure. The variations are the domain, dataset format, and grouping column.

## Verification

Run the automated tests:

```bash
mvn test
```

The 20 test cases check domain validation, UTF-8 and whitespace handling, invalid headers and rows, empty input, missing resources, reading failures, unchanged summaries after reordering, matching results from file, URL, and memory inputs, and matching file and memory outputs. They also check that borrowed streams stay open and invalid input preserves an existing output file.

The URL adapter is tested with a local file URL, so tests do not need an Internet connection. The default execution prints the same summary shown above and writes it to summary.txt.

## Video

Pending: add the explanatory video link (maximum duration: 7 minutes).
