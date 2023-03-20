package com.transfer.betransferapp.dto;

import java.math.BigDecimal;

public class TransferDto {

    private String allowanceAccountNumber;
    private String restaurantAccountNumber;
    private BigDecimal amount;

    public String getAllowanceAccountNumber() {
        return allowanceAccountNumber;
    }

    public void setAllowanceAccountNumber(String allowanceAccountNumber) {
        this.allowanceAccountNumber = allowanceAccountNumber;
    }

    public String getRestaurantAccountNumber() {
        return restaurantAccountNumber;
    }

    public void setRestaurantAccountNumber(String restaurantAccountNumber) {
        this.restaurantAccountNumber = restaurantAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
