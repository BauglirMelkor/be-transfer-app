package com.transfer.betransferapp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;
import com.transfer.betransferapp.service.AllowanceService;
import com.transfer.betransferapp.service.RestaurantAccountService;

@SpringBootApplication
public class BeTransferAppApplication {

    @Autowired
    private AllowanceService allowanceService;

    @Autowired
    private RestaurantAccountService restaurantAccountService;

    public static void main(String[] args) {
        SpringApplication.run(BeTransferAppApplication.class, args);
    }

}
