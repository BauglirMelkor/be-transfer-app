package com.transfer.betransferapp.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.transfer.betransferapp.dto.ResultingAccount;
import com.transfer.betransferapp.dto.TransferDto;
import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;
import com.transfer.betransferapp.exception.AllowanceAccountNotFound;
import com.transfer.betransferapp.exception.InsufficientFunds;
import com.transfer.betransferapp.exception.RestaurantAccountNotFound;

@Service
public class TransferService {

    private final AllowanceService allowanceService;
    private final RestaurantAccountService restaurantAccountService;
    private final MappingService mappingService;

    public TransferService(AllowanceService allowanceService, RestaurantAccountService restaurantAccountService, MappingService mappingService) {
        this.allowanceService = allowanceService;
        this.restaurantAccountService = restaurantAccountService;
        this.mappingService = mappingService;
    }

    public synchronized ResultingAccount transfer(TransferDto transferDto)
        throws AllowanceAccountNotFound, RestaurantAccountNotFound, InsufficientFunds {

        AllowanceAccount allowanceAccount = allowanceService.getAccountByNumber(transferDto.getAllowanceAccountNumber());
        RestaurantAccount restaurantAccount = restaurantAccountService.getAccountByNumber(transferDto.getRestaurantAccountNumber());
        BigDecimal remainingAmount = allowanceAccount.getAmount().subtract(transferDto.getAmount());

        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFunds();
        }

        allowanceAccount.setAmount(remainingAmount);
        BigDecimal newAmount = restaurantAccount.getAmount().add(transferDto.getAmount());
        restaurantAccount.setAmount(newAmount);

        ResultingAccount resultingAccount = new ResultingAccount();
        resultingAccount.setAllowanceAccountDto(mappingService.allowanceAccountEntityToDTO(allowanceService.saveAccount(allowanceAccount)));
        resultingAccount.setRestaurantAccountDto(mappingService.restaurantAccountEntityToDTO(restaurantAccountService.saveAccount(restaurantAccount)));
        resultingAccount.setTransferredAmount(transferDto.getAmount());
        return resultingAccount;
    }

}
