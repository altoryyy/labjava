package recipeservice.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import recipeservice.model.Recipe;

@Repository
public class RecipeDaoImpl implements RecipeDao {
    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeDaoImpl() {
        recipes.add(new Recipe(1L, "Спагетти", "Итальянская"));
        recipes.add(new Recipe(2L, "Суши", "Японская"));
        recipes.add(new Recipe(3L, "Тако", "Мексиканская"));
    }

    @Override
    public List<Recipe> findByCuisine(String cuisine) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCuisine().equalsIgnoreCase(cuisine)) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }

    @Override
    public List<Recipe> findAll() {
        return recipes;
    }

    @Override
    public List<Recipe> findByName(String name) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getName().equalsIgnoreCase(name)) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}