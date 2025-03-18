package recipeservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;
import recipeservice.model.Review;

@Repository
@Transactional
public class ReviewDaoImpl implements ReviewDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Review> getAllReviews() {
        return entityManager.createQuery("FROM Review", Review.class).getResultList();
    }

    @Override
    public Review getReviewById(Long id) {
        return entityManager.find(Review.class, id);
    }

    @Override
    public Review createReview(Review review) {
        entityManager.persist(review);
        return review;
    }

    @Override
    public Review updateReview(Long id, Review review) {
        review.setId(id);
        return entityManager.merge(review);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        if (review != null) {
            entityManager.remove(review);
        }
    }
}