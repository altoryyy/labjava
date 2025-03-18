package recipeservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import recipeservice.dao.ReviewDao;
import recipeservice.dto.ReviewDto;
import recipeservice.model.Review;

@Service
public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewDao.getAllReviews();
        return reviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getText(),
                        review.getRating(),
                        review.getRecipe().getId()))
                .collect(Collectors.toList());
    }

    public ReviewDto getReviewById(Long id) {
        Review review = reviewDao.getReviewById(id);
        return review != null
                ? new ReviewDto(
                        review.getId(),
                        review.getText(),
                        review.getRating(),
                        review.getRecipe().getId())
                : null;
    }

    public Review createReview(Review review) {
        return reviewDao.createReview(review);
    }

    public Review updateReview(Long id, Review review) {
        return reviewDao.updateReview(id, review);
    }

    public void deleteReview(Long id) {
        reviewDao.deleteReview(id);
    }
}
