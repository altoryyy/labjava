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
import recipeservice.model.Cuisine;
import recipeservice.service.CuisineService;

@RestController
@RequestMapping("/api/cuisines")
public class CuisineController {

    private final CuisineService cuisineService;

    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GetMapping
    public List<Cuisine> getAllCuisines() {
        return cuisineService.getAllCuisines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuisine> getCuisineById(@PathVariable Long id) {
        Cuisine cuisine = cuisineService.getCuisineById(id);
        return cuisine != null ? ResponseEntity.ok(cuisine) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Cuisine> createCuisine(@RequestBody Cuisine cuisine) {
        Cuisine createdCuisine = cuisineService.createCuisine(cuisine);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCuisine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuisine> updateCuisine(
            @PathVariable Long id,
            @RequestBody Cuisine cuisine) {
        Cuisine updatedCuisine = cuisineService.updateCuisine(id, cuisine);
        return updatedCuisine != null
                ? ResponseEntity.ok(updatedCuisine)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        cuisineService.deleteCuisine(id);
        return ResponseEntity.noContent().build();
    }
}