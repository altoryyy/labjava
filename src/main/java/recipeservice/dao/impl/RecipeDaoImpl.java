package recipeservice.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;
import recipeservice.dao.RecipeDao;
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

    @Override
    public List<Recipe> findRecipesByIngredientId(Long ingredientId) {
        return entityManager.createQuery(
                "SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.id = :ingredientId",
                        Recipe.class)
                .setParameter("ingredientId", ingredientId)
                .getResultList();
    }

    //jpql

    @Override
    public List<Recipe> findRecipesByCuisineIdJpql(Long cuisineId) {
        return entityManager.createQuery(
                "SELECT r FROM Recipe r WHERE r.cuisine.id = :cuisineId",
                        Recipe.class)
                .setParameter("cuisineId", cuisineId)
                .getResultList();
    }

    //native

    @Override
    @SuppressWarnings("unchecked")
    public List<Recipe> findRecipesByCuisineIdNative(Long cuisineId) {
        return entityManager.createNativeQuery(
                "SELECT r.* FROM recipe r WHERE r.cuisine_id = :cuisineId",
                        Recipe.class)
                .setParameter("cuisineId", cuisineId)
                .getResultList();
    }
}