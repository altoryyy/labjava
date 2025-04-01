package recipeservice.dao;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recipeservice.model.Recipe;

public interface RecipeDao {
    List<Recipe> getAllRecipes();

    List<Recipe> findRecipesByIngredientId(Long ingredientId);

    Recipe getRecipeById(Long id);

    Recipe createRecipe(Recipe recipe);

    Recipe updateRecipe(Long id, Recipe recipe);

    void deleteRecipe(Long id);

    @Query("SELECT r FROM Recipe r WHERE r.cuisine.id = :cuisineId")
    List<Recipe> findRecipesByCuisineIdJpql(@Param("cuisineId") Long cuisineId);

    @Query(value = "SELECT * FROM recipe r WHERE r.cuisine_id = :cuisineId", nativeQuery = true)
    List<Recipe> findRecipesByCuisineIdNative(@Param("cuisineId") Long cuisineId);
}