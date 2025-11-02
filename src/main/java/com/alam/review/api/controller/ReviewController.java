package com.alam.review.api.controller;

import com.alam.review.api.dto.ReviewDto;
import com.alam.review.api.exception.InvalidReviewException;
import com.alam.review.api.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review/")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/saveReview")
    public ResponseEntity<ReviewDto> saveReview(@Valid  @RequestBody ReviewDto reviewDto,
                                                BindingResult bindingResult) {
        log.info("ReviewService :- saveReview(){}", LocalDateTime.now());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            log.error("DTO Validation failed saveReview :: {}", errorMessage);
            throw new InvalidReviewException("ReviewService:saveReview ::  Validation failed: " + errorMessage);
        }
        return ResponseEntity.ok(reviewService.addReview(reviewDto));
    }

    @GetMapping("/getAllReviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        log.info("ReviewService :- getAllReviews(){}", LocalDateTime.now());
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
    @GetMapping("/getReviewById/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("reviewId") Long id) {
        log.info("ReviewService :- getReviewById(){}", LocalDateTime.now());
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    @GetMapping("/getReviewsByProductId/{productName}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable("productName") String productName) {
        log.info("ReviewService :- getReviewsByProductId(){}", LocalDateTime.now());
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productName));
    }
    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("reviewId") Long id,
                                                  @Valid @RequestBody ReviewDto reviewDto,BindingResult bindingResult) {
        log.info("ReviewService :- updateReview(){}", LocalDateTime.now());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            log.error("DTO Validation failed to updateReview :: {}", errorMessage);
            throw new InvalidReviewException("ReviewService:saveReview ::  Validation failed: " + errorMessage);
        }
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDto));
    }
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long id) {
        log.info("ReviewService :- deleteReview(){}", LocalDateTime.now());
        return ResponseEntity.ok("Record is deleted");
    }
}
