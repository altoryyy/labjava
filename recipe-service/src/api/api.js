import axios from 'axios';

const API_URL = 'http://localhost:8080/api/recipes';

export const fetchRecipes = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const createReview = async (reviewData) => {
    console.log('Sending review data:', reviewData);
    const response = await fetch('http://localhost:8080/api/reviews', {
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
    const response = await fetch('http://localhost:8080/api/recipes', {
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
    const response = await fetch('http://localhost:8080/api/ingredients');
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const fetchCuisines = async () => {
    const response = await fetch('http://localhost:8080/api/cuisines'); // Предположим, что этот эндпоинт существует
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Ошибка: ${errorData.message}`);
    }
    return await response.json();
};

export const createIngredient = async (ingredientData) => {
    const response = await fetch('http://localhost:8080/api/ingredients', {
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