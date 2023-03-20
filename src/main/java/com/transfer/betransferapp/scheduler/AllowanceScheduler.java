package com.transfer.betransferapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.transfer.betransferapp.service.AllowanceService;

@Component
public class AllowanceScheduler {

    private final AllowanceService allowanceService;

    public AllowanceScheduler(AllowanceService allowanceService){
        this.allowanceService = allowanceService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateAllowance() {
        allowanceService.resetAccount();
    }
}
