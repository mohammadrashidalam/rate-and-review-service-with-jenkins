package com.alam.review.api.controlleradvice;

import com.alam.review.api.dto.CustomResponseDto;
import com.alam.review.api.dto.ReviewDto;
import com.alam.review.api.exception.DuplicateReviewFoundException;
import com.alam.review.api.exception.ReviewNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ReviewServiceGlobalException {
    @ExceptionHandler(ReviewNotFoundException.class)
    ResponseEntity<CustomResponseDto<ReviewDto>> handleReviewNotFoundException(ReviewNotFoundException ex){
        log.debug("BrandServiceGlobalExceptionHandler : handleReviewNotFoundException{}",LocalDateTime.now());
        CustomResponseDto<ReviewDto> errorResponse= CustomResponseDto.<ReviewDto>builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode("400")
                .errorMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build()  ;
        log.error("BrandServiceGlobalExceptionHandler::handleBrandNotFoundException caught {}",ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(DuplicateReviewFoundException.class)
    ResponseEntity<CustomResponseDto<ReviewDto>> handleDuplicateReviewFoundException(DuplicateReviewFoundException ex){
        log.debug("BrandServiceGlobalExceptionHandler : handleDuplicateReviewFoundException {}",LocalDateTime.now());
        CustomResponseDto<ReviewDto> errorResponse= CustomResponseDto.<ReviewDto>builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode("400")
                .errorMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build() ;
        log.error("BrandServiceGlobalExceptionHandler::handleDuplicateBrandIdException caught {}",ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
