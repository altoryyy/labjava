package recipeservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import recipeservice.dao.CuisineDao;
import recipeservice.model.Cuisine;

@Service
public class CuisineService {

    private final CuisineDao cuisineDao;

    public CuisineService(CuisineDao cuisineDao) {
        this.cuisineDao = cuisineDao;
    }

    public List<Cuisine> getAllCuisines() {
        return cuisineDao.getAllCuisines();
    }

    public Cuisine getCuisineById(Long id) {
        return cuisineDao.getCuisineById(id);
    }

    public Cuisine createCuisine(Cuisine cuisine) {
        return cuisineDao.createCuisine(cuisine);
    }

    public Cuisine updateCuisine(Long id, Cuisine cuisine) {
        return cuisineDao.updateCuisine(id, cuisine);
    }

    public void deleteCuisine(Long id) {
        cuisineDao.deleteCuisine(id);
    }
}