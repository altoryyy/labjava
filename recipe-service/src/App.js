import './App.css';
import React, { useEffect, useState } from 'react';
import { Layout, Button, Modal, Input, Select, Spin } from 'antd';
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
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const loadInitialData = async () => {
            try {
                setLoading(true);
                const [ingredients, cuisines] = await Promise.all([
                    fetchIngredients(),
                    fetchCuisines()
                ]);
                setIngredientOptions(ingredients);
                setCuisineOptions(cuisines);
                await refreshRecipes();
            } catch (error) {
                console.error('Ошибка при инициализации:', error);
            } finally {
                setLoading(false);
            }
        };
        loadInitialData();
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
        <Layout style={{ minHeight: '100vh', background: '#f7f7f7' }}>
            <Header style={{
                display: 'flex',
                alignItems: 'center',
                position: 'fixed',
                width: '100%',
                zIndex: 1,
                backgroundColor: '#fff',
                boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)'
            }}>
                <div className="logo" style={{ fontSize: '24px', color: '#58a36c', fontWeight: 'bold', marginRight: 'auto' }}>
                    🍽️ Рецепты
                </div>
                <Button
                    className="add-recipe-button"
                    type="primary"
                    onClick={handleAddRecipe}
                    style={{
                        backgroundColor: '#58a36c', // Зеленый оттенок
                        borderColor: '#58a36c',
                        color: 'white',  // Текст белого цвета
                        fontWeight: 'bold',
                        transition: 'all 0.3s ease',
                        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)', // Тень
                        borderRadius: '8px',  // Округление углов
                    }}
                >
                    Добавить рецепт
                </Button>
            </Header>
            <Content style={{ padding: '60px 50px 50px 50px', marginTop: 64 }}>
                <div style={{ padding: 24, minHeight: 280, background: '#fff', borderRadius: 12, boxShadow: '0 2px 6px rgba(0,0,0,0.05)' }}>
                    {loading ? <Spin size="large" /> : <RecipeList recipes={recipes} refreshRecipes={refreshRecipes} />}
                </div>
            </Content>

            <Modal
                title="Добавить рецепт"
                open={visibleAddRecipe}
                onCancel={handleCancelAddRecipe}
                onOk={submitRecipe}
                okText="Сохранить"
                cancelText="Отмена"
                style={{ maxWidth: '600px', padding: '20px' }} // Увеличено пространство
                bodyStyle={{
                    padding: '24px', // Паддинг для внутренних элементов
                    backgroundColor: '#fff', // Сделано белым
                    border: 'none', // Убрали лишнюю границу
                }}
                okButtonProps={{
                    style: {
                        backgroundColor: '#58a36c',
                        borderColor: '#58a36c',
                        color: 'white',
                    },
                }}
            >
                <div style={{ marginBottom: '16px' }}>
                    <Input
                        style={{ marginBottom: 10 }}
                        placeholder="Название рецепта"
                        value={newRecipeTitle}
                        onChange={e => setNewRecipeTitle(e.target.value)}
                    />
                    <Input.TextArea
                        style={{ marginBottom: 10 }}
                        placeholder="Описание рецепта"
                        value={newRecipeDescription}
                        onChange={e => setNewRecipeDescription(e.target.value)}
                    />
                    <Select
                        mode="multiple"
                        style={{ width: '100%', marginBottom: 10 }}
                        placeholder="Выберите ингредиенты"
                        value={newRecipeIngredients}
                        onChange={setNewRecipeIngredients}
                    >
                        {ingredientOptions.map(ingredient => (
                            <Option key={ingredient.id} value={ingredient.id}>{ingredient.name}</Option>
                        ))}
                    </Select>
                    <Select
                        style={{ width: '100%', marginBottom: 10 }}
                        placeholder="Выберите кухню"
                        value={newCuisineId}
                        onChange={setNewCuisineId}
                    >
                        {cuisineOptions.map(cuisine => (
                            <Option key={cuisine.id} value={cuisine.id}>{cuisine.name}</Option>
                        ))}
                    </Select>
                    <Input
                        style={{ marginBottom: 10 }}
                        placeholder="Добавить новый ингредиент"
                        value={newIngredientName}
                        onChange={e => setNewIngredientName(e.target.value)}
                        onPressEnter={handleAddIngredient}
                    />
                    <Button
                        type="primary"
                        onClick={handleAddIngredient}
                        block
                        style={{
                            backgroundColor: '#58a36c', // Зелёный цвет
                            borderColor: '#58a36c',
                            color: 'white',
                            fontWeight: 'bold',
                        }}
                    >
                        Добавить ингредиент
                    </Button>
                </div>
            </Modal>
        </Layout>
    );
};

export default App;
