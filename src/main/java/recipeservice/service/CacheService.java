package recipeservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import recipeservice.dto.RecipeDto;

@Service
public class CacheService {
    private final Map<String, List<RecipeDto>> cache = new HashMap<>();

    public List<RecipeDto> getCachedRecipes(String key) {
        return cache.get(key);
    }

    public void cacheRecipes(String key, List<RecipeDto> recipes) {
        cache.put(key, recipes);
    }
}