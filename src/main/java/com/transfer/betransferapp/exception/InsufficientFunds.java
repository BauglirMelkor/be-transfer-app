package com.transfer.betransferapp.exception;

public class InsufficientFunds extends Exception {

    public InsufficientFunds() {
        super("Account doesn't have enough funds to transfer");
    }
}
