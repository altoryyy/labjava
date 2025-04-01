package recipeservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import recipeservice.dao.RecipeDao;
import recipeservice.dto.CuisineDto;
import recipeservice.dto.IngredientDto;
import recipeservice.dto.RecipeDto;
import recipeservice.dto.ReviewDto;
import recipeservice.model.Cuisine;
import recipeservice.model.Ingredient;
import recipeservice.model.Recipe;

@Service
public class RecipeService {

    private final RecipeDao recipeDao;
    private final CacheService cacheService;

    public RecipeService(RecipeDao recipeDao, CacheService cacheService) {
        this.recipeDao = recipeDao;
        this.cacheService = cacheService;
    }

    public List<RecipeDto> getAllRecipes() {
        List<Recipe> recipes = recipeDao.getAllRecipes();
        return recipes.stream()
                .map(recipe -> {
                    List<IngredientDto> ingredientDtos = (recipe.getIngredients() != null)
                            ? recipe.getIngredients().stream()
                            .map(ingredient ->
                                    new IngredientDto(ingredient.getId(), ingredient.getName()))
                            .toList() : List.of();

                    List<ReviewDto> reviewDtos = (recipe.getReviews() != null)
                            ? recipe.getReviews().stream()
                            .map(review ->
                                    new ReviewDto(review.getId(),
                                            review.getText(),
                                            review.getRating(),
                                            recipe.getId()))
                            .toList() : List.of();

                    // Проверка на null для cuisine
                    Cuisine cuisine = recipe.getCuisine();
                    CuisineDto cuisineDto = (cuisine != null)
                            ? new CuisineDto(cuisine.getId(), cuisine.getName())
                            : null; // Или можно создать CuisineDto с пустыми значениями

                    return new RecipeDto(
                            recipe.getId(),
                            recipe.getTitle(),
                            recipe.getDescription(),
                            ingredientDtos,
                            reviewDtos,
                            cuisineDto // Передаем CuisineDto
                    );
                })
                .toList();
    }

    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeDao.getRecipeById(id);
        return recipe != null ? convertToDto(recipe) : null;
    }

    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Recipe recipe = convertToEntity(recipeDto);
        Recipe createdRecipe = recipeDao.createRecipe(recipe);
        return convertToDto(createdRecipe);
    }

    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe = convertToEntity(recipeDto);
        recipe.setId(id);
        Recipe updatedRecipe = recipeDao.updateRecipe(id, recipe);
        return updatedRecipe != null ? convertToDto(updatedRecipe) : null;
    }

    public void deleteRecipe(Long id) {
        recipeDao.deleteRecipe(id);
    }

    private RecipeDto convertToDto(Recipe recipe) {
        List<IngredientDto> ingredientDtos = (recipe.getIngredients() != null)
                ? recipe.getIngredients().stream()
                .map(ingredient -> new IngredientDto(
                        ingredient.getId(),
                        ingredient.getName()))
                .toList() : List.of();

        List<ReviewDto> reviewDtos = (recipe.getReviews() != null)
                ? recipe.getReviews().stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getText(),
                        review.getRating(),
                        recipe.getId()))
                .toList() : List.of();

        // Проверка на null для cuisine
        Cuisine cuisine = recipe.getCuisine();
        CuisineDto cuisineDto = (cuisine != null)
                ? new CuisineDto(cuisine.getId(), cuisine.getName())
                : null; // Или можно создать CuisineDto с пустыми значениями

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                ingredientDtos,
                reviewDtos,
                cuisineDto // Передаем CuisineDto
        );
    }

    private Recipe convertToEntity(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());

        if (recipeDto.getCuisine() != null) {
            Cuisine cuisine = new Cuisine();
            cuisine.setId(recipeDto.getCuisine().getId());
            recipe.setCuisine(cuisine);
        }

        List<Ingredient> ingredients = (recipeDto.getIngredients() != null)
                ? recipeDto.getIngredients().stream()
                .map(ingredientDto -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(ingredientDto.getId());
                    return ingredient;
                })
                .toList() : List.of();

        recipe.setIngredients(ingredients);
        return recipe;
    }

    public List<RecipeDto> getRecipesByCuisineId(Long cuisineId) {
        List<RecipeDto> cachedRecipes = cacheService.getCachedRecipes(cuisineId.toString());
        if (cachedRecipes != null) {
            return cachedRecipes;
        }

        List<Recipe> recipes = recipeDao.findRecipesByCuisineIdJpql(cuisineId);
        if (recipes.isEmpty()) {
            recipes = recipeDao.findRecipesByCuisineIdNative(cuisineId);
        }

        List<RecipeDto> recipeDtos = recipes.stream().map(this::convertToDto).toList();
        cacheService.cacheRecipes(cuisineId.toString(), recipeDtos);

        return recipeDtos;
    }
}