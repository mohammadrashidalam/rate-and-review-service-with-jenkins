package com.alam.review.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class RateAndReviewServiceApplicationTests {

	@Test
	void contextLoads() {
		log.info("RateAndReviewServiceApplicationTests :: contextLoads(){}", LocalDateTime.now());
        Assertions.assertTrue(true);
	}

}
