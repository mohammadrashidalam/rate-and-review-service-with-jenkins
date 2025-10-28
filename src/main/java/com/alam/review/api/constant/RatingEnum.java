package com.alam.review.api.constant;

import lombok.Getter;

@Getter
public enum RatingEnum {
    ONE("TERRIBLE"),
    TWO("BAD"),
    THREE("OKAY"),
    FOUR("GOOD"),
    FIVE("GREAT");

    private final String value;

    RatingEnum(String value) {
        this.value = value;
    }

    public static RatingEnum fromValue(String value) {
        for (RatingEnum rating : RatingEnum.values()) {
            if (rating.value.equals(value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}
