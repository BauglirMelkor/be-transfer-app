package com.transfer.betransferapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transfer.betransferapp.dto.ResultingAccount;
import com.transfer.betransferapp.dto.TransferDto;
import com.transfer.betransferapp.exception.AllowanceAccountNotFound;
import com.transfer.betransferapp.exception.InsufficientFunds;
import com.transfer.betransferapp.exception.RestaurantAccountNotFound;
import com.transfer.betransferapp.service.TransferService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(summary = "Transfer allowance to the restaurant's account")
    @PostMapping
    public ResponseEntity<ResultingAccount> transferBetweenAccounts(@RequestBody TransferDto transferDto)
        throws AllowanceAccountNotFound, RestaurantAccountNotFound, InsufficientFunds {
        return ResponseEntity.ok(transferService.transfer(transferDto));
    }
}
