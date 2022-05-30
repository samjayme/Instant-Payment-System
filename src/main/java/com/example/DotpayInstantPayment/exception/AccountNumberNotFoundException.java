package com.example.DotpayInstantPayment.exception;

public class AccountNumberNotFoundException extends RuntimeException{

    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}
