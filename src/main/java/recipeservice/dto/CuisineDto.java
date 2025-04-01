package recipeservice.dto;

public class CuisineDto {
    private final Long id;
    private final String name;

    public CuisineDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}