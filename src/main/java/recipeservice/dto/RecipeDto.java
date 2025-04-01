package recipeservice.dto;

import java.util.List;

public class RecipeDto {
    private final Long id;
    private final String title;
    private final String description;
    private final List<IngredientDto> ingredients;
    private final List<ReviewDto> reviews;
    private final CuisineDto cuisine;

    public RecipeDto(
            Long id, String title,
            String description,
            List<IngredientDto> ingredients,
            List<ReviewDto> reviews,
            CuisineDto cuisine) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.reviews = reviews;
        this.cuisine = cuisine;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public CuisineDto getCuisine() {
        return cuisine;
    }
}