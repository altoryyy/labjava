package recipeservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;
import recipeservice.model.Recipe;

@Repository
@Transactional
public class RecipeDaoImpl implements RecipeDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Recipe> getAllRecipes() {
        return entityManager.createQuery("FROM Recipe", Recipe.class).getResultList();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return entityManager.find(Recipe.class, id);
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        entityManager.persist(recipe);
        return recipe;
    }

    @Override
    public Recipe updateRecipe(Long id, Recipe recipe) {
        recipe.setId(id);
        return entityManager.merge(recipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        if (recipe != null) {
            entityManager.remove(recipe);
        }
    }
}