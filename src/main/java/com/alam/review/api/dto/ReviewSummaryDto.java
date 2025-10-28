package com.alam.review.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSummaryDto {
    private double averageRating;                   // e.g., 4.3
    private long totalRatings;                      // e.g., 31,585
    private long totalReviews;                      // e.g., 2,387
    private Map<Integer, Long> ratingDistribution; // 5★->20063, 4★->6572...
    private Map<String, Double> categoryAverage;   // Camera->4.1, Battery->4.2, Display->4.4
    private List<ReviewDto> recentReviews;         // Last few reviews
}
