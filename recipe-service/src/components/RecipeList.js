import React, { useState, useEffect } from 'react';
import { Card, Rate, Button, Modal, Input, Divider, Select, Collapse } from 'antd';
import { createReview, deleteRecipe, updateRecipe, fetchIngredients, fetchCuisines } from '../api/api';
import { EditOutlined, DeleteOutlined, PlusCircleOutlined, SearchOutlined } from '@ant-design/icons';
import { motion, AnimatePresence } from 'framer-motion';

const { Option } = Select;
const { Search } = Input;

const RecipeList = ({ recipes, refreshRecipes }) => {
    // Состояния для управления UI
    const [visibleAddReview, setVisibleAddReview] = useState(false);
    const [visibleUpdateRecipe, setVisibleUpdateRecipe] = useState(false);
    const [visibleDeleteConfirm, setVisibleDeleteConfirm] = useState(false);
    const [selectedRecipe, setSelectedRecipe] = useState(null);

    // Состояния для отзывов
    const [newReviewText, setNewReviewText] = useState('');
    const [newReviewRating, setNewReviewRating] = useState(0);
    const [currentRecipeId, setCurrentRecipeId] = useState(null);

    // Состояния для обновления рецепта
    const [updateRecipeTitle, setUpdateRecipeTitle] = useState('');
    const [updateRecipeDescription, setUpdateRecipeDescription] = useState('');
    const [updateRecipeIngredients, setUpdateRecipeIngredients] = useState([]);
    const [updateCuisineId, setUpdateCuisineId] = useState(null);

    // Состояния для данных
    const [ingredientOptions, setIngredientOptions] = useState([]);
    const [cuisineOptions, setCuisineOptions] = useState([]);
    const [recipeToDelete, setRecipeToDelete] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    // Загрузка опций для ингредиентов и кухонь
    const loadOptions = async () => {
        try {
            const [ingredients, cuisines] = await Promise.all([
                fetchIngredients(),
                fetchCuisines(),
            ]);
            setIngredientOptions(ingredients);
            setCuisineOptions(cuisines);
        } catch (error) {
            console.error('Ошибка при загрузке опций:', error);
        }
    };

    useEffect(() => {
        loadOptions();
    }, []);


    const theme = {
        primaryColor: '#58a36c',
        secondaryColor: '#ffffff',
        accentColor: '#3d7a4d',
        textColor: '#333333',
        lightBg: '#f8f8f8',
        cardShadow: '0 4px 12px rgba(0, 0, 0, 0.08)'
    };

    // Анимации
    const cardAnimation = {
        initial: { opacity: 0, y: 20 },
        animate: { opacity: 1, y: 0 },
        exit: { opacity: 0, scale: 0.95 },
        transition: { duration: 0.3 }
    }

    const searchAnimation = {
        initial: { opacity: 0, y: -20 },
        animate: { opacity: 1, y: 0 },
        transition: { duration: 0.4, delay: 0.1 }
    };

    // Фильтрация рецептов по поисковому запросу
    const filteredRecipes = recipes.filter(recipe => {
        const term = searchTerm.toLowerCase();
        return (
            recipe.title.toLowerCase().includes(term) ||
            recipe.description.toLowerCase().includes(term) ||
            recipe.ingredients.some(i => i.name.toLowerCase().includes(term)) ||
            recipe.cuisine.name.toLowerCase().includes(term)
        );
    });

    // Расчет среднего рейтинга
    const calculateAverageRating = (reviews) => {
        if (!reviews.length) return 0;
        const total = reviews.reduce((acc, review) => acc + review.rating, 0);
        return total / reviews.length;
    };

    // Управление отзывами
    const handleAddReview = (recipeId, e) => {
        e.stopPropagation();
        setCurrentRecipeId(recipeId);
        setVisibleAddReview(true);
    };

    const handleCancelAddReview = () => {
        setVisibleAddReview(false);
        setNewReviewText('');
        setNewReviewRating(0);
    };

    const submitReview = async () => {
        if (!newReviewText || newReviewRating === 0) return;

        const reviewData = {
            text: newReviewText,
            rating: newReviewRating,
            recipe: { id: currentRecipeId },
        };

        try {
            await createReview(reviewData);
            handleCancelAddReview();
            await refreshRecipes();
        } catch (error) {
            console.error('Ошибка при создании отзыва:', error);
        }
    };

    // Просмотр полного рецепта
    const showFullRecipe = (recipe) => {
        setSelectedRecipe(recipe);
    };

    // Управление рецептами
    const handleDeleteRecipe = (recipeId, e) => {
        e.stopPropagation();
        setRecipeToDelete(recipeId);
        setVisibleDeleteConfirm(true);
    };

    const confirmDeleteRecipe = async () => {
        try {
            await deleteRecipe(Number(recipeToDelete));
            await refreshRecipes();
            setVisibleDeleteConfirm(false);
            setRecipeToDelete(null);
        } catch (error) {
            console.error('Ошибка при удалении рецепта:', error);
        }
    };

    const handleOpenUpdateRecipe = (recipe, e) => {
        e.stopPropagation();
        e.preventDefault();
        setUpdateRecipeTitle(recipe.title);
        setUpdateRecipeDescription(recipe.description);
        setUpdateRecipeIngredients(recipe.ingredients.map(i => i.id));
        setUpdateCuisineId(recipe.cuisine.id);
        setSelectedRecipe(null);
        setVisibleUpdateRecipe(true);
    };

    const submitUpdateRecipe = async () => {
        const updatedRecipeData = {
            title: updateRecipeTitle,
            description: updateRecipeDescription,
            ingredients: updateRecipeIngredients.map(id => ({ id })),
            cuisine: { id: updateCuisineId },
        };

        try {
            await updateRecipe(selectedRecipe?.id, updatedRecipeData);
            await refreshRecipes();
            setVisibleUpdateRecipe(false);
        } catch (error) {
            console.error('Ошибка при обновлении рецепта:', error);
        }
    };

    // Стили для карточек
    const descriptionBlockStyle = {
        width: '100%',
        minHeight: '60px',
        backgroundColor: '#ffffff',
        borderRadius: '8px',
        padding: '8px',
        //boxShadow: '0 1px 4px rgba(0, 0, 0, 0.08)',
        overflow: 'hidden',
        boxSizing: 'border-box',
        marginBottom: '8px',
        transform: 'translateX(-8px)',
    };

    const ingredientsBlockStyle = {
        width: '100%',
        minHeight: '60px',
        backgroundColor: '#ffffff',
        borderRadius: '8px',
        padding: '8px',
        //boxShadow: '0 1px 4px rgba(0, 0, 0, 0.08)',
        overflow: 'hidden',
        boxSizing: 'border-box',
        marginBottom: '8px',
        transform: 'translateX(-8px)',
    };

    const cuisineBlockStyle = {
        width: '100%',
        minHeight: '40px',
        backgroundColor: '#ffffff',
        borderRadius: '8px',
        padding: '8px',
        //boxShadow: '0 1px 4px rgba(0, 0, 0, 0.08)',
        overflow: 'hidden',
        boxSizing: 'border-box',
        marginBottom: '8px',
        transform: 'translateX(-8px)',
    };

    const descriptionTextStyle = {
        margin: 0,
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        display: '-webkit-box',
        WebkitLineClamp: 2,
        WebkitBoxOrient: 'vertical',
        minHeight: '40px',
    };

    const ingredientsTextStyle = {
        margin: 0,
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        display: '-webkit-box',
        WebkitLineClamp: 2,
        WebkitBoxOrient: 'vertical',
        minHeight: '40px'
    };

    const cuisineTextStyle = {
        margin: 0,
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        display: '-webkit-box',
        WebkitLineClamp: 1,
        WebkitBoxOrient: 'vertical'
    };

    const containerStyle = {
        backgroundColor: theme.secondaryColor,
        borderRadius: '12px',
        padding: '24px',
        boxShadow: theme.cardShadow,
        marginBottom: '24px'
    };

    const searchContainerStyle = {
        ...containerStyle,
        marginBottom: '24px'
    };

    const recipesContainerStyle = {
        ...containerStyle,
        backgroundColor: '#ffffff',
    };



    return (
        <div style={{ marginTop: '30px', padding: '0 16px' }}>
            {/* Блок поиска с анимацией */}
            <motion.div
                style={searchContainerStyle}
                {...searchAnimation}
            >
                <Search
                    placeholder="Поиск рецептов..."
                    allowClear
                    enterButton={<SearchOutlined />}
                    size="large"
                    onChange={e => setSearchTerm(e.target.value)}
                    value={searchTerm}
                    style={{
                        width: '100%',
                        borderColor: theme.primaryColor
                    }}
                    className="custom-search-btn"
                />
            </motion.div>

            {/* Список рецептов */}
            <div style={recipesContainerStyle}>
                <AnimatePresence>
                    <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '24px' }}>
                        {filteredRecipes.map(recipe => (
                            <motion.div
                                key={recipe.id}
                                layout
                                {...cardAnimation}
                            >
                                <Card
                                    title={recipe.title}
                                    style={{
                                        width: 300,
                                        height: 485,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        backgroundColor: theme.secondaryColor,
                                        border: 'none',
                                        borderRadius: '12px',
                                        boxShadow: theme.cardShadow,
                                        padding: '16px',
                                        transition: 'transform 0.2s',
                                    }}
                                    onMouseEnter={e => (e.currentTarget.style.transform = 'scale(1.02)')}
                                    onMouseLeave={e => (e.currentTarget.style.transform = 'scale(1)')}
                                    hoverable
                                    onClick={() => showFullRecipe(recipe)}
                                >
                        <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                            <div style={descriptionBlockStyle}>
                                <strong>Описание:</strong>
                                <p style={descriptionTextStyle}>
                                    {recipe.description}
                                </p>
                            </div>

                            <div style={ingredientsBlockStyle}>
                                <strong>Ингредиенты:</strong>
                                <p style={ingredientsTextStyle}>
                                    {recipe.ingredients.map(i => i.name).join(', ')}
                                </p>
                            </div>

                            <div style={cuisineBlockStyle}>
                                <strong>Кухня:</strong>
                                <p style={cuisineTextStyle}>
                                    {recipe.cuisine.name}
                                </p>
                            </div>

                            <div style={{ marginTop: 10 }}>
                                <p style={{ margin: '0 0 4px' }}><strong>Отзывы:</strong> {recipe.reviews.length}</p>
                                <Rate disabled defaultValue={calculateAverageRating(recipe.reviews)} style={{ marginBottom: 12 }} />
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-start', gap: '8px', marginTop: '12px' }}>
                            <Button
                                icon={<PlusCircleOutlined />}
                                type="primary"
                                onClick={(e) => handleAddReview(recipe.id, e)}
                                style={{
                                    backgroundColor: '#58a36c',
                                    borderColor: '#58a36c',
                                    color: 'white',
                                }}
                            />
                            <Button
                                icon={<EditOutlined />}
                                type="default"
                                onClick={(e) => handleOpenUpdateRecipe(recipe, e)}
                                style={{
                                    backgroundColor: '#ffffff',
                                    borderColor: '#58a36c',
                                    color: '#58a36c',
                                }}
                            />
                            <Button
                                icon={<DeleteOutlined />}
                                type="danger"
                                onClick={(e) => handleDeleteRecipe(recipe.id, e)}
                                style={{
                                    backgroundColor: '#ffffff',
                                    borderColor: '#58a36c',
                                    color: 'green',
                                }}
                            />
                        </div>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                </AnimatePresence>
            </div>


            {/* Модальное окно просмотра рецепта */}
            <Modal
                title={selectedRecipe?.title || ''}
                open={!!selectedRecipe}
                onCancel={() => setSelectedRecipe(null)}
                footer={null}
            >
                {selectedRecipe && (
                    <div>
                        <p><strong>Описание:</strong> {selectedRecipe.description}</p>
                        <p><strong>Ингредиенты:</strong> {selectedRecipe.ingredients.map(i => i.name).join(', ')}</p>
                        <p><strong>Кухня:</strong> {selectedRecipe.cuisine.name}</p>
                        <Divider />
                        <h4>Отзывы:</h4>
                        {selectedRecipe.reviews.map(review => (
                            <div key={review.id} style={{ marginBottom: '12px' }}>
                                <Rate disabled defaultValue={review.rating} />
                                <p>{review.text}</p>
                            </div>
                        ))}
                    </div>
                )}
            </Modal>

            {/* Модальное окно добавления отзыва */}
            <Modal
                title="Добавить отзыв"
                open={visibleAddReview}
                onCancel={handleCancelAddReview}
                onOk={submitReview}
                okText="Сохранить"
                cancelText="Отмена"
                okButtonProps={{ style: { backgroundColor: '#58a36c', borderColor: '#58a36c', color: 'white' } }}
            >
                <Input.TextArea
                    rows={4}
                    value={newReviewText}
                    onChange={(e) => setNewReviewText(e.target.value)}
                    placeholder="Введите текст отзыва"
                    style={{ marginBottom: '12px' }}
                />
                <Rate
                    onChange={setNewReviewRating}
                    value={newReviewRating}
                />
            </Modal>

            {/* Модальное окно обновления рецепта */}
            <Modal
                title="Обновить рецепт"
                open={visibleUpdateRecipe}
                onCancel={() => setVisibleUpdateRecipe(false)}
                onOk={submitUpdateRecipe}
                okText="Сохранить"
                cancelText="Отмена"
                okButtonProps={{ style: { backgroundColor: '#58a36c', borderColor: '#58a36c', color: 'white' } }}
            >
                <Input
                    placeholder="Название рецепта"
                    value={updateRecipeTitle}
                    onChange={e => setUpdateRecipeTitle(e.target.value)}
                    style={{ marginBottom: 10 }}
                />
                <Input.TextArea
                    placeholder="Описание рецепта"
                    value={updateRecipeDescription}
                    onChange={e => setUpdateRecipeDescription(e.target.value)}
                    style={{ marginBottom: 10 }}
                />
                <Select
                    mode="multiple"
                    style={{ width: '100%', marginBottom: 10 }}
                    placeholder="Выберите ингредиенты"
                    value={updateRecipeIngredients}
                    onChange={setUpdateRecipeIngredients}
                >
                    {ingredientOptions.map(ingredient => (
                        <Option key={ingredient.id} value={ingredient.id}>{ingredient.name}</Option>
                    ))}
                </Select>
                <Select
                    style={{ width: '100%', marginBottom: 10 }}
                    placeholder="Выберите кухню"
                    value={updateCuisineId}
                    onChange={setUpdateCuisineId}
                >
                    {cuisineOptions.map(cuisine => (
                        <Option key={cuisine.id} value={cuisine.id}>{cuisine.name}</Option>
                    ))}
                </Select>
            </Modal>

            {/* Модальное окно подтверждения удаления */}
            <Modal
                title="Подтверждение удаления"
                open={visibleDeleteConfirm}
                onCancel={() => setVisibleDeleteConfirm(false)}
                onOk={confirmDeleteRecipe}
                okText="Удалить"
                cancelText="Отмена"
            >
                <p>Вы уверены, что хотите удалить этот рецепт?</p>
            </Modal>
        </div>
    );
};

export default RecipeList;