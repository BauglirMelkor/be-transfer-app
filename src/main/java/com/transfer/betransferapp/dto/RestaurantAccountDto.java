package com.transfer.betransferapp.dto;

import java.math.BigDecimal;
import java.math.MathContext;

public class RestaurantAccountDto {

    private static final MathContext mathContext = new MathContext(5);

    private Long id;

    private String accountNumber;

    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount.round(mathContext);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
