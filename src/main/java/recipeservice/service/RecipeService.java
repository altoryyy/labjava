package recipeservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import recipeservice.dao.RecipeDao;
import recipeservice.model.Recipe;

@Service
public class RecipeService {
    private final RecipeDao recipeDao;

    public RecipeService(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public List<Recipe> getRecipesByCuisine(String cuisine) {
        return recipeDao.findByCuisine(cuisine);
    }

    public List<Recipe> getAllRecipes() {
        return recipeDao.findAll();
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeDao.findByName(name);
    }
}