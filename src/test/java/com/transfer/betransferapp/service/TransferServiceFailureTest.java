package com.transfer.betransferapp.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransferServiceFailureTest {

    private TransferService transferService;

    @Mock
    private AllowanceService allowanceService;

    @Mock
    private RestaurantAccountService restaurantAccountService;

    @Autowired
    private MappingService mappingService;

    @BeforeEach
    void setup() {
        transferService = new TransferService(allowanceService, restaurantAccountService, mappingService);
    }

    @Test
    void transfer_money_allowance_not_found() throws AllowanceAccountNotFound {
        doThrow(new AllowanceAccountNotFound()).when(allowanceService).getAccountByNumber("1");
        TransferDto transferDto = new TransferDto();
        transferDto.setRestaurantAccountNumber("1");
        transferDto.setAllowanceAccountNumber("1");
        transferDto.setAmount(new BigDecimal("8"));
        assertThrows(AllowanceAccountNotFound.class, () -> transferService.transfer(transferDto));
    }

    @Test
    void transfer_money_restaurant_not_found() throws RestaurantAccountNotFound, AllowanceAccountNotFound {
        AllowanceAccount allowanceAccount = new AllowanceAccount();
        allowanceAccount.setAmount(BigDecimal.TEN);
        allowanceAccount.setAccountNumber("1");
        allowanceAccount.setId(1L);
        when(allowanceService.getAccountByNumber("1")).thenReturn(allowanceAccount);

        doThrow(new RestaurantAccountNotFound()).when(restaurantAccountService).getAccountByNumber("1");

        TransferDto transferDto = new TransferDto();
        transferDto.setRestaurantAccountNumber("1");
        transferDto.setAllowanceAccountNumber("1");
        transferDto.setAmount(new BigDecimal("8"));
        assertThrows(RestaurantAccountNotFound.class, () -> transferService.transfer(transferDto));
    }

}
