package com.example.DotpayInstantPayment.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@EnableAsync
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@Slf4j
public class GetAllSuccessFullTransactionScheduler {

    @Autowired
    private DotPayScheduledTaskService dotPayScheduledTaskService;


    @Scheduled(cron= "0 0/2 * * * *")
    @Async
    public void GetAllSuccessFullTransaction(){
        String  GetAllSuccessFullTransaction = dotPayScheduledTaskService.GetSuccessFullTransactionAndCommissionStatus();
        log.info("GETTING ALL SUCCESSFULL TARNSACTIONS AND COMMISSONS",GetAllSuccessFullTransaction);

    }

    @Scheduled(cron= "0 0/5 * * * *")
    @Async
    public void getTransactionSummary() {
        String transactionSummary = dotPayScheduledTaskService.getTransactionSummary();
        log.info("TRANSACTION SUMMARY", transactionSummary);

    }




}
