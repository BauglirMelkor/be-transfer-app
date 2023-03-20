package com.transfer.betransferapp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.transfer.betransferapp.exception.AllowanceAccountNotFound;
import com.transfer.betransferapp.exception.InsufficientFunds;
import com.transfer.betransferapp.exception.RestaurantAccountNotFound;

@ControllerAdvice
public class TransferExceptionHandler {

    @ExceptionHandler(AllowanceAccountNotFound.class)
    @ResponseBody
    public ResponseEntity<Object> handleCAllowanceAccountNotFoundException(AllowanceAccountNotFound ex) {
        return new ResponseEntity<>("Allowance Account not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestaurantAccountNotFound.class)
    @ResponseBody
    public ResponseEntity<Object> handleToppingNotFoundException(RestaurantAccountNotFound ex) {
        return new ResponseEntity<>("Restaurant Account not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFunds.class)
    @ResponseBody
    public ResponseEntity<Object> handleInsufficientException(InsufficientFunds ex) {
        return new ResponseEntity<>("Insufficient Funds", HttpStatus.NOT_ACCEPTABLE);
    }

}