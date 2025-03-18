package recipeservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import recipeservice.dao.IngredientDao;
import recipeservice.dto.IngredientDto;
import recipeservice.model.Ingredient;

@Service
public class IngredientService {

    private final IngredientDao ingredientDao;

    public IngredientService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        return ingredients.stream()
                .map(this::convertToDto)
                .toList();
    }

    public IngredientDto getIngredientById(Long id) {
        Ingredient ingredient = ingredientDao.getIngredientById(id);
        return ingredient != null ? convertToDto(ingredient) : null;
    }

    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = convertToEntity(ingredientDto);
        Ingredient createdIngredient = ingredientDao.createIngredient(ingredient);
        return convertToDto(createdIngredient);
    }

    public IngredientDto updateIngredient(Long id, IngredientDto ingredientDto) {
        Ingredient ingredient = convertToEntity(ingredientDto);
        ingredient.setId(id); // Установите id для обновления
        Ingredient updatedIngredient = ingredientDao.updateIngredient(id, ingredient);
        return updatedIngredient != null ? convertToDto(updatedIngredient) : null;
    }

    public void deleteIngredient(Long id) {
        ingredientDao.deleteIngredient(id);
    }

    // Преобразование Ingredient в IngredientDto
    private IngredientDto convertToDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getName());
    }

    // Преобразование IngredientDto в Ingredient
    private Ingredient convertToEntity(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        return ingredient;
    }
}