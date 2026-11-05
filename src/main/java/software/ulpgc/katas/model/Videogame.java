package software.ulpgc.katas.model;

public record Videogame(
        String rank,
        String name,
        String platform,
        String yearOfRelease,
        String genre,
        String publisher,
        String naSales,
        String euSales,
        String jpSales,
        String otherSales,
        String globalSales
) {
    public Videogame {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Videogame name is required");
        if (platform == null || platform.isBlank()) throw new IllegalArgumentException("Platform is required");
    }
}
