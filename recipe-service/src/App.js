import './App.css';
import React, { useEffect, useState } from 'react';
import { Layout, Button, Modal, Input, Select } from 'antd';
import RecipeList from './components/RecipeList';
import { createRecipe, fetchIngredients, fetchCuisines, createIngredient, fetchRecipes } from './api/api';

const { Header, Content } = Layout;
const { Option } = Select;

const App = () => {
    const [visibleAddRecipe, setVisibleAddRecipe] = useState(false);
    const [newRecipeTitle, setNewRecipeTitle] = useState('');
    const [newRecipeDescription, setNewRecipeDescription] = useState('');
    const [newRecipeIngredients, setNewRecipeIngredients] = useState([]);
    const [ingredientOptions, setIngredientOptions] = useState([]);
    const [cuisineOptions, setCuisineOptions] = useState([]);
    const [newCuisineId, setNewCuisineId] = useState(null);
    const [newIngredientName, setNewIngredientName] = useState('');
    const [recipes, setRecipes] = useState([]);

    useEffect(() => {
        const loadIngredients = async () => {
            try {
                const ingredients = await fetchIngredients();
                setIngredientOptions(ingredients);
            } catch (error) {
                console.error('Ошибка при загрузке ингредиентов:', error);
            }
        };

        const loadCuisines = async () => {
            try {
                const cuisines = await fetchCuisines();
                setCuisineOptions(cuisines);
            } catch (error) {
                console.error('Ошибка при загрузке кухонь:', error);
            }
        };

        const loadRecipes = async () => {
            await refreshRecipes();
        };

        loadIngredients();
        loadCuisines();
        loadRecipes();
    }, []);

    const refreshIngredients = async () => {
        try {
            const ingredients = await fetchIngredients();
            setIngredientOptions(ingredients);
        } catch (error) {
            console.error('Ошибка при обновлении ингредиентов:', error);
        }
    };

    const refreshRecipes = async () => {
        try {
            const recipesData = await fetchRecipes();
            setRecipes(recipesData);
        } catch (error) {
            console.error('Ошибка при обновлении рецептов:', error);
        }
    };

    const handleAddRecipe = () => {
        setVisibleAddRecipe(true);
    };

    const handleCancelAddRecipe = () => {
        setVisibleAddRecipe(false);
        setNewRecipeTitle('');
        setNewRecipeDescription('');
        setNewRecipeIngredients([]);
        setNewIngredientName('');
        setNewCuisineId(null);
    };

    const submitRecipe = async () => {
        const recipeData = {
            title: newRecipeTitle,
            description: newRecipeDescription,
            ingredients: newRecipeIngredients.map(id => ({ id })),
            cuisine: { id: newCuisineId },
        };

        try {
            await createRecipe(recipeData);
            await refreshRecipes();
            handleCancelAddRecipe();
        } catch (error) {
            console.error('Ошибка при добавлении рецепта:', error);
        }
    };

    const handleAddIngredient = async () => {
        if (newIngredientName) {
            try {
                const newIngredient = await createIngredient({ name: newIngredientName });
                console.log('Добавлен новый ингредиент:', newIngredient);
                await refreshIngredients();
                setNewIngredientName('');
            } catch (error) {
                console.error('Ошибка при добавлении ингредиента:', error);
            }
        }
    };

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Layout>
                <Header style={{
                    display: 'flex',
                    alignItems: 'center',
                    position: 'fixed',
                    width: '100%',
                    zIndex: 1,
                    backgroundColor: '#fff',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)'
                }}>
                    <div className="logo" style={{ fontSize: '24px', color: '#000', marginRight: 'auto' }}>
                        Рецепты
                    </div>
                    <Button
                        className="add-recipe-button"
                        type="default"
                        onClick={handleAddRecipe}
                    >
                        Добавить рецепт
                    </Button>
                </Header>
                <Content style={{ padding: '50px', marginTop: 64, background: '#fff' }}>
                    <div style={{ padding: 24, minHeight: 280 }}>
                        <RecipeList recipes={recipes} refreshRecipes={refreshRecipes} />
                    </div>
                </Content>
            </Layout>

            <Modal title="Добавить рецепт" visible={visibleAddRecipe} onCancel={handleCancelAddRecipe} onOk={submitRecipe}>
                <Input placeholder="Название рецепта" value={newRecipeTitle} onChange={e => setNewRecipeTitle(e.target.value)} />
                <Input.TextArea placeholder="Описание рецепта" value={newRecipeDescription} onChange={e => setNewRecipeDescription(e.target.value)} />
                <Select
                    mode="multiple"
                    style={{ width: '100%' }}
                    placeholder="Выберите ингредиенты"
                    value={newRecipeIngredients}
                    onChange={setNewRecipeIngredients}
                >
                    {ingredientOptions.map(ingredient => (
                        <Option key={ingredient.id} value={ingredient.id}>{ingredient.name}</Option>
                    ))}
                </Select>
                <Select
                    style={{ width: '100%', marginTop: '10px' }}
                    placeholder="Выберите кухню"
                    value={newCuisineId}
                    onChange={setNewCuisineId}
                >
                    {cuisineOptions.map(cuisine => (
                        <Option key={cuisine.id} value={cuisine.id}>{cuisine.name}</Option>
                    ))}
                </Select>
                <Input
                    placeholder="Добавить новый ингредиент"
                    value={newIngredientName}
                    onChange={e => setNewIngredientName(e.target.value)}
                    onPressEnter={handleAddIngredient}
                />
                <Button type="primary" onClick={handleAddIngredient}>
                    Добавить ингредиент
                </Button>
            </Modal>
        </Layout>
    );
};

export default App;