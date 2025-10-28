package com.alam.review.api.service;

import com.alam.review.api.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    public ReviewDto addReview(ReviewDto reviewDto);
    public List<ReviewDto> getAllReviews();
    public ReviewDto getReviewById(Long id);
    public List<ReviewDto> getReviewsByProductId(String productName);
    public ReviewDto updateReview(Long id, ReviewDto reviewDto);
    public void deleteReview(Long id);

}
