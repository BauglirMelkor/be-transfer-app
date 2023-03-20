package com.transfer.betransferapp.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.transfer.betransferapp.dto.ResultingAccount;
import com.transfer.betransferapp.dto.TransferDto;
import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;
import com.transfer.betransferapp.exception.AllowanceAccountNotFound;
import com.transfer.betransferapp.exception.InsufficientFunds;
import com.transfer.betransferapp.exception.RestaurantAccountNotFound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransferServiceTest {

    private TransferService transferService;

    @Autowired
    private AllowanceService allowanceService;

    @Autowired
    private RestaurantAccountService restaurantAccountService;

    @Autowired
    private MappingService mappingService;

    @BeforeEach
    void setup() {
        transferService = new TransferService(allowanceService, restaurantAccountService, mappingService);
    }

    @Test
    void transfer_money_successfully() throws AllowanceAccountNotFound, RestaurantAccountNotFound, InsufficientFunds {
        AllowanceAccount allowanceAccount = new AllowanceAccount();
        allowanceAccount.setAmount(BigDecimal.TEN);
        allowanceAccount.setAccountNumber("1");
        allowanceService.saveAccount(allowanceAccount);

        RestaurantAccount restaurantAccount = new RestaurantAccount();
        restaurantAccount.setAmount(BigDecimal.TEN);
        restaurantAccount.setAccountNumber("1");
        restaurantAccountService.saveAccount(restaurantAccount);

        TransferDto transferDto = new TransferDto();
        transferDto.setRestaurantAccountNumber("1");
        transferDto.setAllowanceAccountNumber("1");
        transferDto.setAmount(new BigDecimal("8"));

        ResultingAccount resultingAccount = transferService.transfer(transferDto);
        assertThat(resultingAccount.getRestaurantAccountDto().getAmount()).isEqualTo(new BigDecimal("18"));
        assertThat(resultingAccount.getAllowanceAccountDto().getAmount()).isEqualTo(new BigDecimal("2"));
        assertThat(resultingAccount.getTransferredAmount()).isEqualTo(new BigDecimal("8"));
    }

    @Test
    void transfer_money_insufficient_funds() {
        AllowanceAccount allowanceAccount = new AllowanceAccount();
        allowanceAccount.setAmount(new BigDecimal("9"));
        allowanceAccount.setAccountNumber("2");
        allowanceService.saveAccount(allowanceAccount);

        RestaurantAccount restaurantAccount = new RestaurantAccount();
        restaurantAccount.setAmount(BigDecimal.TEN);
        restaurantAccount.setAccountNumber("2");
        restaurantAccountService.saveAccount(restaurantAccount);

        TransferDto transferDto = new TransferDto();
        transferDto.setRestaurantAccountNumber("2");
        transferDto.setAllowanceAccountNumber("2");
        transferDto.setAmount(new BigDecimal("10"));
        assertThrows(InsufficientFunds.class, () -> transferService.transfer(transferDto));
    }

    @Test
    void transfer_money_funds_topped() throws AllowanceAccountNotFound, RestaurantAccountNotFound, InsufficientFunds {
        AllowanceAccount allowanceAccount = new AllowanceAccount();
        allowanceAccount.setAmount(new BigDecimal("10"));
        allowanceAccount.setAccountNumber("3");
        allowanceService.saveAccount(allowanceAccount);

        RestaurantAccount restaurantAccount = new RestaurantAccount();
        restaurantAccount.setAmount(BigDecimal.TEN);
        restaurantAccount.setAccountNumber("3");
        restaurantAccountService.saveAccount(restaurantAccount);

        TransferDto transferDto = new TransferDto();
        transferDto.setRestaurantAccountNumber("3");
        transferDto.setAllowanceAccountNumber("3");
        transferDto.setAmount(new BigDecimal("10"));

        ResultingAccount resultingAccount = transferService.transfer(transferDto);
        assertThat(resultingAccount.getRestaurantAccountDto().getAmount()).isEqualTo(new BigDecimal("20"));
        assertThat(resultingAccount.getAllowanceAccountDto().getAmount()).isEqualTo(new BigDecimal("0"));
        assertThat(resultingAccount.getTransferredAmount()).isEqualTo(new BigDecimal("10"));

        allowanceService.resetAccount();
        allowanceAccount = allowanceService.getAccountByNumber("3");
        assertThat(allowanceAccount.getAmount()).isEqualTo(new BigDecimal("10.00"));


    }

}
