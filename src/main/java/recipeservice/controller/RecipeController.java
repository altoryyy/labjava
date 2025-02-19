package recipeservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recipeservice.model.Recipe;
import recipeservice.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<Recipe>> getRecipesByCuisine(@PathVariable String cuisine) {
        List<Recipe> recipes = recipeService.getRecipesByCuisine(cuisine);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Возвращаем 404, если нет рецептов
        }
        return ResponseEntity.ok(recipes);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<Recipe>> getRecipesByName(@RequestParam String name) {
        List<Recipe> recipes = recipeService.getRecipesByName(name);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Возвращаем 404, если нет рецептов
        }
        return ResponseEntity.ok(recipes);
    }
}