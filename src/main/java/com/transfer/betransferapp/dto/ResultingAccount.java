package com.transfer.betransferapp.dto;

import java.math.BigDecimal;
import java.math.MathContext;

public class ResultingAccount {

    private static final MathContext mathContext = new MathContext(5);

    private AllowanceAccountDto allowanceAccountDto;

    private RestaurantAccountDto restaurantAccountDto;

    private BigDecimal transferredAmount;

    public AllowanceAccountDto getAllowanceAccountDto() {
        return allowanceAccountDto;
    }

    public void setAllowanceAccountDto(AllowanceAccountDto allowanceAccountDto) {
        this.allowanceAccountDto = allowanceAccountDto;
    }

    public RestaurantAccountDto getRestaurantAccountDto() {
        return restaurantAccountDto;
    }

    public void setRestaurantAccountDto(RestaurantAccountDto restaurantAccountDto) {
        this.restaurantAccountDto = restaurantAccountDto;
    }

    public BigDecimal getTransferredAmount() {
        return transferredAmount.round(mathContext);
    }

    public void setTransferredAmount(BigDecimal transferredAmount) {
        this.transferredAmount = transferredAmount;
    }
}
