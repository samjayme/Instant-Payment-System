package com.example.DotpayInstantPayment.exception;

public class NoTransactionForGivenDate extends RuntimeException  {

    public NoTransactionForGivenDate(String message) {
        super(message);
    }
}
