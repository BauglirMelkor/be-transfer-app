package com.transfer.betransferapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.transfer.betransferapp.entity.RestaurantAccount;

@Repository
public interface RestaurantAccountRepository extends CrudRepository<RestaurantAccount, Long> {

    Optional<RestaurantAccount> findAccountByAccountNumber(String accountNumber);
}
