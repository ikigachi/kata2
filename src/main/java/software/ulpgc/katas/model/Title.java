package software.ulpgc.katas.model;

public record Title(String name, Genre genre) {
    public Title {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Title name is required");
        if (genre == null) throw new IllegalArgumentException("Genre is required");
    }
}
