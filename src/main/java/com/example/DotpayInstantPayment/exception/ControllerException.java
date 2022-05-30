package com.example.DotpayInstantPayment.exception;

import com.example.DotpayInstantPayment.entity.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class ControllerException  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNumberNotFoundException.class)
    public ResponseEntity<Object> handleAccountNumberNotFoundException(AccountNumberNotFoundException e, WebRequest request){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND,
                LocalDate.now()),HttpStatus.NOT_FOUND);


    }
    @ExceptionHandler(NoTransactionForGivenDate.class)
    public ResponseEntity<Object> handleNoTransactionForGivenDateException(NoTransactionForGivenDate e, WebRequest request){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(),HttpStatus.NOT_FOUND,
                LocalDate.now()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND,
                LocalDate.now()), HttpStatus.NOT_FOUND);
    }
}