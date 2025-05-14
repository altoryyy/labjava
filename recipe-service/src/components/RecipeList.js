import React, { useState } from 'react';
import { Card, Rate, Button, Modal, Input, Divider } from 'antd';
import { createReview, deleteRecipe, updateRecipe } from '../api/api';

const RecipeList = ({ recipes, refreshRecipes }) => {
    const [visibleAddReview, setVisibleAddReview] = useState(false);
    const [newReviewText, setNewReviewText] = useState('');
    const [newReviewRating, setNewReviewRating] = useState(0);
    const [currentRecipeId, setCurrentRecipeId] = useState(null);
    const [selectedRecipe, setSelectedRecipe] = useState(null);
    const [isCardOpen, setIsCardOpen] = useState(false);

    const calculateAverageRating = (reviews) => {
        if (!reviews.length) return 0;
        const total = reviews.reduce((acc, review) => acc + review.rating, 0);
        return total / reviews.length;
    };

    const handleAddReview = (recipeId) => {
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

    const showFullRecipe = (recipe) => {
        setIsCardOpen(true);  // Показать карточку с анимацией
        setSelectedRecipe(recipe);
    };

    const handleDeleteRecipe = async (recipeId) => {
        try {
            await deleteRecipe(recipeId);
            await refreshRecipes();  // Обновить список рецептов
        } catch (error) {
            console.error('Ошибка при удалении рецепта:', error);
        }
    };

    const handleUpdateRecipe = async (recipeId) => {
        const updatedRecipeData = {
            title: "Обновленное название", // Тут будет новый заголовок
            description: "Обновленное описание.", // Новое описание
            ingredients: [{ id: 6 }], // Пример обновленных ингредиентов
            cuisine: { id: 6 }, // Обновленная кухня
        };

        try {
            await updateRecipe(recipeId, updatedRecipeData);
            await refreshRecipes();  // Обновить список рецептов
        } catch (error) {
            console.error('Ошибка при обновлении рецепта:', error);
        }
    };

    return (
        <div style={{ marginTop: '20px' }}>
            <div
                style={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'center',
                    gap: '24px',
                }}
            >
                {recipes.map(recipe => (
                    <Card
                        key={recipe.id}
                        title={recipe.title}
                        style={{
                            width: 280,
                            height: 480,
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'space-between',
                            backgroundColor: '#ffffff',
                            border: '1px solid #e1e1e1',
                            borderRadius: '10px',
                            boxShadow: '0 4px 12px rgba(0, 0, 0, 0.06)',
                            padding: '12px',
                        }}
                        className={isCardOpen ? 'card-open' : ''} // Анимация
                        headStyle={{ backgroundColor: '#f6f6f6', color: '#58a36c' }}
                        hoverable
                        onClick={() => showFullRecipe(recipe)}
                    >
                        <div style={{ flex: 1 }}>
                            <div style={{ minHeight: '160px' }}>
                                <p><strong>Описание:</strong> {recipe.description.slice(0, 100)}...</p>
                                <p><strong>Ингредиенты:</strong> {recipe.ingredients.map(i => i.name).join(', ')}</p>
                                <p><strong>Кухня:</strong> {recipe.cuisine.name}</p>
                                <p><strong>Отзывы:</strong> {recipe.reviews.length}</p>
                            </div>
                            <Rate disabled defaultValue={calculateAverageRating(recipe.reviews)} style={{ marginBottom: 10 }} />
                        </div>
                        <Button
                            type="primary"
                            style={{
                                backgroundColor: '#58a36c',
                                borderColor: '#58a36c',
                                width: '100%',
                                alignSelf: 'flex-end'
                            }}
                            onClick={(e) => {
                                e.stopPropagation();
                                handleAddReview(recipe.id);
                            }}
                        >
                            Добавить отзыв
                        </Button>
                        <Button
                            style={{
                                backgroundColor: '#f44336',
                                borderColor: '#f44336',
                                width: '100%',
                                marginTop: 10,
                            }}
                            onClick={(e) => {
                                e.stopPropagation();
                                handleDeleteRecipe(recipe.id);
                            }}
                        >
                            Удалить рецепт
                        </Button>
                        <Button
                            style={{
                                backgroundColor: '#ff9800',
                                borderColor: '#ff9800',
                                width: '100%',
                                marginTop: 10,
                            }}
                            onClick={(e) => {
                                e.stopPropagation();
                                handleUpdateRecipe(recipe.id);
                            }}
                        >
                            Обновить рецепт
                        </Button>
                    </Card>
                ))}
            </div>

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

            <Modal
                title="Добавить отзыв"
                open={visibleAddReview}
                onCancel={handleCancelAddReview}
                onOk={submitReview}
                okText="Сохранить"
                cancelText="Отмена"
                okButtonProps={{ style: { backgroundColor: '#58a36c', borderColor: '#58a36c' } }}
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
        </div>
    );
};

export default RecipeList;
