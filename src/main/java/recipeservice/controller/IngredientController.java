package recipeservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import recipeservice.dto.IngredientDto;
import recipeservice.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Long id) {
        IngredientDto ingredient = ingredientService.getIngredientById(id);
        return ingredient != null
                ? ResponseEntity.ok(ingredient)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(
            @RequestBody IngredientDto ingredientDto) {
        IngredientDto createdIngredient = ingredientService.createIngredient(ingredientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(
            @PathVariable Long id,
            @RequestBody IngredientDto ingredientDto) {
        IngredientDto updatedIngredient = ingredientService.updateIngredient(id, ingredientDto);
        return updatedIngredient != null
                ? ResponseEntity.ok(updatedIngredient)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}