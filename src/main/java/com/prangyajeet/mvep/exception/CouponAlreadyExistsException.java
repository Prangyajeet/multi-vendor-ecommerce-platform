package com.prangyajeet.mvep.exception;

public class CouponAlreadyExistsException extends RuntimeException {

    public CouponAlreadyExistsException(String message) {
        super(message);
    }
}