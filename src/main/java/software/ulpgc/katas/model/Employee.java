package software.ulpgc.katas.model;

public record Employee(String name, Gender gender) {
    public Employee {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Employee name is required");
        if (gender == null) throw new IllegalArgumentException("Gender is required");
    }
}
