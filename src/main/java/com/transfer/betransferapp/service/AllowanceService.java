package com.transfer.betransferapp.service;

import java.math.BigDecimal;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.exception.AllowanceAccountNotFound;
import com.transfer.betransferapp.repository.AllowanceAccountRepository;

@Service
public class AllowanceService {

    private final AllowanceAccountRepository allowanceAccountRepository;

    public AllowanceService(AllowanceAccountRepository allowanceAccountRepository) {
        this.allowanceAccountRepository = allowanceAccountRepository;
        for (int i = 0; i < 100; i++) {
            AllowanceAccount allowanceAccount = new AllowanceAccount();
            allowanceAccount.setAmount(new BigDecimal("10"));
            allowanceAccount.setAccountNumber("a" + i);
            saveAccount(allowanceAccount);
        }
    }

    @Cacheable(cacheNames = { "allowanceCache" }, key = "#accountNumber", sync=true)
    public AllowanceAccount getAccountByNumber(String accountNumber) throws AllowanceAccountNotFound {
        return allowanceAccountRepository.findAccountByAccountNumber(accountNumber)
            .orElseThrow(AllowanceAccountNotFound::new);
    }

    @CachePut(cacheNames = { "allowanceCache" }, key = "#allowanceAccount.accountNumber")
    public AllowanceAccount saveAccount(AllowanceAccount allowanceAccount) {
        return allowanceAccountRepository.save(allowanceAccount);
    }

    @CacheEvict(cacheNames = { "allowanceCache" }, allEntries = true)
    public void resetAccount() {
        allowanceAccountRepository.resetAllAccounts();
    }
}
