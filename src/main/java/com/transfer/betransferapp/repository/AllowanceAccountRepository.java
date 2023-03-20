package com.transfer.betransferapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;

@Repository
@Transactional
public interface AllowanceAccountRepository extends CrudRepository<AllowanceAccount, Long> {

    Optional<AllowanceAccount> findAccountByAccountNumber(String accountNumber);

    @Modifying
    @Query(value = "update allowance_account set amount = 10", nativeQuery = true)
    void resetAllAccounts();
}
