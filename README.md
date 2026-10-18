# Kata 2

## Objective

Read tabular data through an InputStream, transform each row into a domain object, calculate a summary, and write it through an OutputStream. Reading, calculation, and writing are separated.

## Compilation and execution

Run from the project root:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main"
```

The program reads data.tsv and writes the employee count by gender to the console and summary.txt:

```text
Female: 2
Male: 1
```

To use another file or a URL, pass its path or address as an argument:

```bash
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.Main" -Dexec.args="employees.tsv"
```

## Dependencies and JDK

- JDK 27
- Maven

## Project Structure

Base package: software.ulpgc.katas.

- Main.java: connects the input, summary calculation, and output.
- model/Employee.java: stores an employee's name and gender.
- model/Gender.java: defines Male and Female.
- io/EmployeeReader.java: loading interface and TSV parsing methods.
- io/FileEmployeeReader.java: reads from a file.
- io/UrlEmployeeReader.java: reads from a URL.
- EmployeeSummary.java: counts employees by gender.
- io/SummaryWriter.java: writes the summary to an OutputStream.
- data.tsv: small dataset for the default execution.

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

For the included employee dataset, the expected result is Female: 2 and Male: 1, as shown above.

## Video

Pending: add the explanatory video link (maximum duration: 7 minutes).
