# Kata 2

## Objective

Read tabular data through an InputStream, transform each row into a domain object, calculate a summary, and write it through an OutputStream. Reading, calculation, and writing are separated.

## Compilation and execution

Run from the project root:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main"
```

The program reads data.csv and writes the book count by author to the console and summary.txt:

```text
Becquer: 1
Cervantes: 2
```

To use another file or a URL, pass its path or address as an argument:

```bash
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main" -Dexec.args="books.csv"
```

## Dependencies and JDK

- JDK 27
- Maven

## Project Structure

Base package: software.ulpgc.katas.

- Main.java: connects the input, summary calculation, and output.
- model/Book.java: stores a book's author and title.
- io/BookReader.java: loading interface and CSV parsing methods.
- io/FileBookReader.java: reads from a file.
- io/UrlBookReader.java: reads from a URL.
- BookSummary.java: counts books by author.
- io/SummaryWriter.java: writes the summary to an OutputStream.
- data.csv: small dataset for the default execution.

## Repetitions

The kata was repeated three times in separate branches:

- `master`: employees grouped by gender.
- `book-version`: books grouped by author.
- `videogame-version`: videogames grouped by platform.

Each version keeps the same reader, entity, summary, and writer structure. The variations are the domain, dataset format, and grouping column.

## Verification

All three versions were compiled with JDK 27 and checked using temporary verification code:

- File, local HTTP URL, and ByteArrayInputStream inputs produced the same summary.
- Console, file, and ByteArrayOutputStream outputs matched.
- Reordering the data rows did not change the summary.
- Empty input produced an empty summary.
- Missing files and URLs produced controlled input errors.
- Malformed rows produced an error with the row number.

For the included book dataset, the expected result is Becquer: 1 and Cervantes: 2, as shown above.

## Video

Pending: add the explanatory video link (maximum duration: 7 minutes).
