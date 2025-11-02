package com.alam.review.api.dto;

import jakarta.validation.constraints.*;
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
        @NotNull(message = "reviewId cannot be null")
        @Positive(message = "reviewId must be a positive number")
        private Long reviewId;
        @Pattern.List({
                @Pattern(regexp = "^[^<>/]*$", message = "Input must not contain <, >, or / symbols"),
                @Pattern(regexp = "^[A-Z][A-Za-z\\s]*$",message = "Invalid productName")
        })
        @NotBlank(message = "productName cannot be blank")
        @Size(min = 3,max = 30, message = "productName must be range from 3 to 30")
        private String productName;
        @Pattern.List({
                @Pattern(regexp = "^[^<>/]*$", message = "Input must not contain <, >, or / symbols"),
                @Pattern(regexp = "^[A-Z][A-Za-z\\s]*$",message = "Invalid userName")
        })
        @NotBlank(message = "userName cannot be blank")
        @Size(min = 3,max = 30, message = "userName must be range from 3 to 30")
        private String userName;
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must not exceed 5")
        private int rating;
        @Pattern.List({
                @Pattern(regexp = "^[^<>/]*$", message = "Input must not contain <, >, or / symbols"),
                @Pattern(regexp = "^[A-Z][A-Za-z\\s]*$",message = "Invalid comments")
        })
        @NotBlank(message = "comments cannot be blank")
        private String comments;
}
