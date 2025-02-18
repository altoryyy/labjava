package recipeservice.dao;

import java.util.List;
import recipeservice.model.Recipe;

public interface RecipeDao {
    List<Recipe> findByCuisine(String cuisine);

    List<Recipe> findAll();

    List<Recipe> findByName(String name);
}