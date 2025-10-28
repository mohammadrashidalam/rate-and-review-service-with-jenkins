package com.alam.review.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "review")
public class Review {
    @Id
    private Long reviewId;
    private String productName;
    private String userName;
    private int rating;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
