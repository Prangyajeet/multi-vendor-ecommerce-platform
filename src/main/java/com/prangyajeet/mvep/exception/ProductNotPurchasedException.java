package com.prangyajeet.mvep.exception;

public class ProductNotPurchasedException extends RuntimeException {

    public ProductNotPurchasedException(String message) {
        super(message);
    }
}