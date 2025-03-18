package recipeservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import recipeservice.dao.RecipeDao;
import recipeservice.dto.IngredientDto;
import recipeservice.dto.RecipeDto;
import recipeservice.dto.ReviewDto;
import recipeservice.model.Ingredient;
import recipeservice.model.Recipe;


@Service
public class RecipeService {

    private final RecipeDao recipeDao;

    public RecipeService(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public List<RecipeDto> getAllRecipes() {
        List<Recipe> recipes = recipeDao.getAllRecipes();
        return recipes.stream()
                .map(recipe -> {
                    List<IngredientDto> ingredientDtos = (recipe.getIngredients() != null)
                            ? recipe.getIngredients().stream()
                                    .map(ingredient
                                            -> new IngredientDto(
                                                    ingredient.getId(),
                                                    ingredient.getName()))
                                    .collect(Collectors.toList()) : List.of();

                    List<ReviewDto> reviewDtos = (recipe.getReviews() != null)
                            ? recipe.getReviews().stream()
                                    .map(review -> new ReviewDto(
                                            review.getId(),
                                            review.getText(),
                                            review.getRating(),
                                            recipe.getId()))
                                    .collect(Collectors.toList()) : List.of();

                    return new RecipeDto(
                            recipe.getId(),
                            recipe.getTitle(),
                            recipe.getDescription(),
                            ingredientDtos,
                            reviewDtos);
                })
                .collect(Collectors.toList());
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
                        .collect(Collectors.toList()) : List.of();

        List<ReviewDto> reviewDtos = (recipe.getReviews() != null)
                ? recipe.getReviews().stream()
                        .map(review -> new ReviewDto(review.getId(),
                                review.getText(), review.getRating(),
                                recipe.getId()))
                        .collect(Collectors.toList()) : List.of();

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                ingredientDtos,
                reviewDtos);
    }

    private Recipe convertToEntity(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());


        List<Ingredient> ingredients = (recipeDto.getIngredients() != null)
                ? recipeDto.getIngredients().stream()
                        .map(ingredientDto -> {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setId(ingredientDto.getId());
                            return ingredient;
                        })
                        .collect(Collectors.toList()) : List.of();

        recipe.setIngredients(ingredients);
        return recipe;
    }
}