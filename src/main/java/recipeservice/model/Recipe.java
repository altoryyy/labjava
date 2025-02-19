package recipeservice.model;

public class Recipe {
    private final Long id;
    private final String name;
    private final String cuisine;

    public Recipe(Long id, String name, String cuisine) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }
}