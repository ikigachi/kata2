package software.ulpgc.katas.model;

public record Book(String author, String title) {
    public Book {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author is required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Book title is required");
    }
}
