package recipeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeservice.dto.ReviewDto;
import recipeservice.exception.CustomException;
import recipeservice.model.Review;
import recipeservice.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы",
            description = "Возвращает список всех отзывов в системе.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно получены все отзывы"),
    })
    public List<ReviewDto> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID",
            description = "Возвращает отзыв с указанным ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Отзыв найден"),
        @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public ReviewDto getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв",
            description = "Создает новый отзыв с заданными параметрами.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Отзыв успешно создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    })
    public Review createReview(@RequestBody Review review) {
        if (review == null || review.getText() == null) {
            throw new CustomException("Содержимое отзыва не может быть пустым");
        }
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв по ID",
            description = "Обновляет существующий отзыв с указанным ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
        @ApiResponse(responseCode = "404", description = "Отзыв не найден"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    })
    public Review updateReview(@PathVariable Long id, @RequestBody Review review) {
        if (review == null || review.getText() == null) {
            throw new CustomException("Содержимое отзыва не может быть пустым");
        }
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв по ID",
            description = "Удаляет отзыв с указанным ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Отзыв успешно удален"),
        @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

}