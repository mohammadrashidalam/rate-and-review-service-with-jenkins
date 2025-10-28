package com.alam.review.api.controller;

import com.alam.review.api.dto.ReviewDto;
import com.alam.review.api.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<ReviewDto> saveReview(@RequestBody ReviewDto reviewDto) {
        log.info("ReviewService :- saveReview(){}", LocalDateTime.now());
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
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("reviewId") Long id, @RequestBody ReviewDto reviewDto) {
        log.info("ReviewService :- updateReview(){}", LocalDateTime.now());
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDto));
    }
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long id) {
        log.info("ReviewService :- deleteReview(){}", LocalDateTime.now());
        return ResponseEntity.ok("Record is deleted");
    }
}
