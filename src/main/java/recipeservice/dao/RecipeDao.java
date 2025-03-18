package recipeservice.dao;

import java.util.List;
import recipeservice.model.Recipe;

public interface RecipeDao {
    List<Recipe> getAllRecipes();

    Recipe getRecipeById(Long id);

    Recipe createRecipe(Recipe recipe);

    Recipe updateRecipe(Long id, Recipe recipe);

    void deleteRecipe(Long id);
}