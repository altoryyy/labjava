package recipeservice.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recipeservice.model.Recipe;
import recipeservice.service.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/cuisine/{cuisine}")
    public List<Recipe> getRecipesByCuisine(@PathVariable String cuisine) {
        return recipeService.getRecipesByCuisine(cuisine);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping(params = "name") // Обрабатываем запросы с параметром name
    public List<Recipe> getRecipesByName(@RequestParam String name) {
        return recipeService.getRecipesByName(name);
    }
}