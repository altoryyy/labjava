import React, { useState } from 'react';
import { Card, Rate, Button, Modal, Input, Divider } from 'antd';
import { createReview } from '../api/api';

const RecipeList = ({ recipes, refreshRecipes }) => {
    const [visibleAddReview, setVisibleAddReview] = useState(false);
    const [newReviewText, setNewReviewText] = useState('');
    const [newReviewRating, setNewReviewRating] = useState(0);
    const [currentRecipeId, setCurrentRecipeId] = useState(null);
    const [selectedRecipe, setSelectedRecipe] = useState(null);

    const calculateAverageRating = (reviews) => {
        if (!reviews.length) return 0;
        const total = reviews.reduce((acc, review) => acc + review.rating, 0);
        return total / reviews.length;
    };

    const handleAddReview = () => {
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
        setSelectedRecipe(recipe);
    };

    return (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '16px' }}>
            {recipes.map(recipe => (
                <Card
                    key={recipe.id}
                    title={recipe.title}
                    style={{ width: 240, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '400px' }}
                    hoverable
                    onClick={() => showFullRecipe(recipe)}
                >
                    <div style={{ flex: 1 }}>
                        <p style={{ minHeight: '60px', maxHeight: '60px', overflow: 'hidden' }}>
                            {recipe.description.length > 100 ? `${recipe.description.slice(0, 100)}...` : recipe.description}
                        </p>
                        <p style={{ minHeight: '40px' }}><strong>Ингредиенты:</strong> {recipe.ingredients.map(ing => ing.name).join(', ')}</p>
                        <p style={{ minHeight: '20px' }}><strong>Кухня:</strong> {recipe.cuisine.name}</p>
                        <p style={{ minHeight: '20px' }}><strong>Отзывы:</strong> {recipe.reviews.length}</p>
                        <Rate disabled defaultValue={calculateAverageRating(recipe.reviews)} />
                    </div>
                    <Button type="default" onClick={(e) => { e.stopPropagation(); handleAddReview(); }} style={{ marginTop: '8px', alignSelf: 'stretch' }}>
                        Добавить отзыв
                    </Button>
                </Card>
            ))}

            <Modal
                title={selectedRecipe ? selectedRecipe.title : ''}
                visible={!!selectedRecipe}
                onCancel={() => setSelectedRecipe(null)}
                footer={null}
            >
                {selectedRecipe && (
                    <div>
                        <p><strong>Описание:</strong> {selectedRecipe.description}</p>
                        <p><strong>Ингредиенты:</strong> {selectedRecipe.ingredients.map(ing => ing.name).join(', ')}</p>
                        <p><strong>Кухня:</strong> {selectedRecipe.cuisine.name}</p>
                        <p><strong>Отзывы:</strong> {selectedRecipe.reviews.length}</p>
                        <Rate disabled defaultValue={calculateAverageRating(selectedRecipe.reviews)} />
                        <Divider style={{ backgroundColor: '#e0e0e0', margin: '16px auto', width: '80%', flex: '0 0 auto' }} />
                        <h3>Список отзывов:</h3>
                        {selectedRecipe.reviews.map(review => (
                            <div key={review.id} style={{ marginBottom: '10px' }}>
                                <p><strong>Оценка:</strong> <Rate disabled defaultValue={review.rating} /></p>
                                <p>{review.text}</p>
                            </div>
                        ))}
                    </div>
                )}
            </Modal>

            <Modal
                title="Добавить отзыв"
                visible={visibleAddReview}
                onCancel={handleCancelAddReview}
                onOk={submitReview}
            >
                <Input.TextArea
                    rows={4}
                    value={newReviewText}
                    onChange={(e) => setNewReviewText(e.target.value)}
                    placeholder="Введите текст отзыва"
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