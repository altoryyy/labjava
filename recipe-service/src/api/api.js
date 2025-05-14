import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const fetchRecipes = async () => {
    const response = await axios.get(`${API_URL}/recipes`);
    return response.data;
};

export const createReview = async (reviewData) => {
    console.log('Sending review data:', reviewData);
    const response = await fetch(`${API_URL}/reviews`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(reviewData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const createRecipe = async (recipeData) => {
    const response = await fetch(`${API_URL}/recipes`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(recipeData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const fetchIngredients = async () => {
    const response = await fetch(`${API_URL}/ingredients`);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const fetchCuisines = async () => {
    const response = await fetch(`${API_URL}/cuisines`);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const createIngredient = async (ingredientData) => {
    const response = await fetch(`${API_URL}/ingredients`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(ingredientData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const deleteRecipe = async (recipeId) => {
    const response = await fetch(`${API_URL}/recipes/${recipeId}`, {
        method: 'DELETE',
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка при удалении рецепта: ${errorData.message}`);
    }

    return await response.json();
};

export const updateRecipe = async (recipeId, updatedRecipeData) => {
    const response = await fetch(`${API_URL}/recipes/${recipeId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedRecipeData),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка при обновлении рецепта: ${errorData.message}`);
    }

    return await response.json();
};
