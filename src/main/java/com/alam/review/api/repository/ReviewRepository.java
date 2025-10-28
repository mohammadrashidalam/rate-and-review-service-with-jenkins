package com.alam.review.api.repository;

import com.alam.review.api.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review,Long> {
    List<Review> findByProductName(String productName);
}
