package com.transfer.betransferapp.service;

import java.math.BigDecimal;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.transfer.betransferapp.entity.RestaurantAccount;
import com.transfer.betransferapp.exception.RestaurantAccountNotFound;
import com.transfer.betransferapp.repository.RestaurantAccountRepository;

@Service
public class RestaurantAccountService {

    private final RestaurantAccountRepository restaurantAccountRepository;

    public RestaurantAccountService(RestaurantAccountRepository restaurantAccountRepository) {
        this.restaurantAccountRepository = restaurantAccountRepository;
        for (int i = 0; i < 100; i++) {
            RestaurantAccount restaurantAccount = new RestaurantAccount();
            restaurantAccount.setAmount(BigDecimal.ZERO);
            restaurantAccount.setAccountNumber("r"+i);
            saveAccount(restaurantAccount);
        }
    }

    @Cacheable(cacheNames = { "restaurantCache" }, key = "#accountNumber", sync=true)
    public RestaurantAccount getAccountByNumber(String accountNumber) throws RestaurantAccountNotFound {
        return restaurantAccountRepository.findAccountByAccountNumber(accountNumber)
            .orElseThrow(RestaurantAccountNotFound::new);
    }

    @CachePut(cacheNames = { "restaurantCache" }, key = "#restaurantAccount.accountNumber")
    public RestaurantAccount saveAccount(RestaurantAccount restaurantAccount) {
        return restaurantAccountRepository.save(restaurantAccount);
    }
}
