package com.alam.review.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
        private Long reviewId;
        private String productName;
        private String userName;
        private int rating;
        private String comments;
}
