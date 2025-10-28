package com.alam.review.api.service.impl;

import com.alam.review.api.dto.ReviewDto;
import com.alam.review.api.entity.Review;
import com.alam.review.api.exception.DuplicateReviewFoundException;
import com.alam.review.api.exception.ReviewNotFoundException;
import com.alam.review.api.repository.ReviewRepository;
import com.alam.review.api.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(final ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * @param reviewDto:reviewDto
     * @return ReviewDto
     */
    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        log.info("ReviewServiceImpl :- addReview(){}", LocalDateTime.now());
        Review review = new Review();
        BeanUtils.copyProperties(reviewDto, review);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        log.info("Review object "+review);
        boolean exists = reviewRepository.existsById(review.getReviewId());
        if (exists) {
            log.error("ReviewServiceImpl :- addReview :: review id already exists {}", review.getReviewId());
            throw new DuplicateReviewFoundException("Category ID already exists: " + review.getReviewId());
        }
        Review review1 = reviewRepository.save(review);
        BeanUtils.copyProperties(review, reviewDto);
        return reviewDto;
    }

    /**
     * @return List<ReviewDto>
     */
    @Override
    public List<ReviewDto> getAllReviews() {
        log.info("ReviewServiceImpl :: getAllReviews(){}", LocalDateTime.now());
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            log.error("ReviewServiceImpl::getAllReviews ->  No Review found:- {}", LocalDateTime.now());
            throw new ReviewNotFoundException("No Review found:. " + reviews);
        }

        return reviews.stream().map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            BeanUtils.copyProperties(review, reviewDto);
            return reviewDto;
        }).toList();
    }

    /**
     * @param id:id
     * @return ReviewDto
     */
    @Override
    public ReviewDto getReviewById(Long id) {
        log.info("ReviewServiceImpl :: getReviewById() {}", LocalDateTime.now());
        Review review = reviewRepository.findById(id).orElseThrow(() -> {
            log.error("ReviewServiceImpl:: getReviewById -> Review not found with ID::- {}", LocalDateTime.now());
            return new ReviewNotFoundException("Review not found with ID: " + id);
        });
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review, reviewDto);
        return reviewDto;
    }

    /*
     * @param productId:productId
     * @return List<ReviewDto>
     */
    @Override
    public List<ReviewDto> getReviewsByProductId(String productName) {
        log.info("ReviewServiceImpl :: getReviewsByProductId() {}", LocalDateTime.now());
        List<Review> reviews = reviewRepository.findByProductName(productName);
        if (reviews.isEmpty()) {
            log.error("ReviewServiceImpl::getReviewsByProductId ->  No Review found with product id:- {}", LocalDateTime.now());
            throw new ReviewNotFoundException("No Review found with product id:. " + reviews);
        }
        return reviews.stream().map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            BeanUtils.copyProperties(review, reviewDto);
            return reviewDto;
        }).toList();
    }

    /**
     * @param id:id
     * @param reviewDto:reviewDto
     * @return ReviewDto
     */
    @Override
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        log.info("ReviewServiceImpl :: updateReview() {}", LocalDateTime.now());
        Review reviewCheck=reviewRepository.findById(id).orElseThrow(() -> {
            log.error("ReviewServiceImpl:: updateReview -> Review not found with ID::- {}", LocalDateTime.now());
            return new ReviewNotFoundException("Review not found with ID: " + id);
        });
        Review review = new Review();
        BeanUtils.copyProperties(reviewDto, review);
        review.setUpdatedAt(reviewCheck.getCreatedAt());
        review.setUpdatedAt(LocalDateTime.now());
        log.info("Review object "+review);
        Review updatedReview = reviewRepository.save(review);
        BeanUtils.copyProperties(review, reviewDto);
        return reviewDto;
    }

    /**
     * @param id:id
     */
    @Override
    public void deleteReview(Long id) {
        log.info("ReviewServiceImpl :: deleteReview() {}", LocalDateTime.now());
        reviewRepository.findById(id).orElseThrow(() -> {
            log.error("ReviewServiceImpl:: deleteReview -> Review not found with ID::- {}", LocalDateTime.now());
            return new ReviewNotFoundException("Review not found with ID: " + id);
        });
        reviewRepository.deleteById(id);
    }
}
