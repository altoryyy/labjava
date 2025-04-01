package recipeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeservice.dto.RecipeDto;
import recipeservice.exception.CustomException;
import recipeservice.service.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @Operation(summary = "Получить все рецепты")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно получены все рецепты"),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public List<RecipeDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить рецепт по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Рецепт найден"),
        @ApiResponse(responseCode = "404", description = "Рецепт не найден")
    })
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        RecipeDto recipe = recipeService.getRecipeById(id);
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Создать новый рецепт")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Рецепт успешно создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    })
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeDto recipeDto) {
        if (recipeDto == null
                || recipeDto.getTitle() == null
                || recipeDto.getDescription() == null) {
            throw new CustomException("Название и описание рецепта не могут быть пустыми");
        }
        RecipeDto createdRecipe = recipeService.createRecipe(recipeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить рецепт по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Рецепт успешно обновлен"),
        @ApiResponse(responseCode = "404", description = "Рецепт не найден"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    })
    public ResponseEntity<RecipeDto> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeDto recipeDto) {
        if (recipeDto == null
                || recipeDto.getTitle() == null
                || recipeDto.getDescription() == null) {
            throw new CustomException("Название и описание рецепта не могут быть пустыми");
        }
        RecipeDto updatedRecipe = recipeService.updateRecipe(id, recipeDto);
        return updatedRecipe != null
                ? ResponseEntity.ok(updatedRecipe)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить рецепт по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Рецепт успешно удален"),
        @ApiResponse(responseCode = "404", description = "Рецепт не найден")
    })
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cuisine/{cuisineId}")
    @Operation(summary = "Получить рецепты по ID кухни")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Успешно получены рецепты для указанной кухни"),
        @ApiResponse(responseCode = "404",
                description = "Кухня не найдена")
    })
    public List<RecipeDto> getRecipesByCuisineId(@PathVariable Long cuisineId) {
        return recipeService.getRecipesByCuisineId(cuisineId);
    }
}