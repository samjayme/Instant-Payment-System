package com.example.DotpayInstantPayment.controller;

import com.example.DotpayInstantPayment.entity.Account;
import com.example.DotpayInstantPayment.entity.Transaction;
import com.example.DotpayInstantPayment.exception.AccountNumberNotFoundException;
import com.example.DotpayInstantPayment.exception.NoTransactionForGivenDate;
import com.example.DotpayInstantPayment.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dotpay")
@Slf4j
@ComponentScan
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping({"/createAccount"})
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account){
        log.info("creating account "+ account);
        return new ResponseEntity<>(accountService.createAccount(account) , HttpStatus.CREATED);

    }

    @PostMapping({"/addMoney"})
    public  ResponseEntity<Account> addMoney(@RequestParam("accountNumber") Long accountNumber, @RequestBody BigDecimal amount) throws AccountNumberNotFoundException {
        log.info("adding money to account " + accountNumber);
        return new ResponseEntity<>(accountService.addMoney(accountNumber,amount), HttpStatus.ACCEPTED);
    }

    @PostMapping({"/transfer"})
    public ResponseEntity<Account> transferMoney(@RequestParam("MasterAccount") Long MasterAccount,
                                                 @RequestParam("RecipientAccount") Long RecipientAccount,
                                                 @RequestParam("Description") String Description,
                                                 @RequestBody BigDecimal amount) throws AccountNumberNotFoundException {

        log.info("Transfer request initiated on " + RecipientAccount);
        return new ResponseEntity<>(accountService.transfer(MasterAccount,RecipientAccount,amount,Description),HttpStatus.OK);

    }

    @GetMapping({"/transactions"})
    public ResponseEntity<List<Transaction>> getTransactions(){
        log.info("Checking all Transactions");
        return  new ResponseEntity<>(accountService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping({"/getAllTransactionByDate"})
    public ResponseEntity<List<Transaction>> getTransactionOnDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NoTransactionForGivenDate {

        log.info("Getting transactions for "+ date);
        return  new ResponseEntity<>(accountService.getAllTransactionByDate(date), HttpStatus.OK);
    }


    @GetMapping({"/getAllTransactionSummaryByDate"})
    public ResponseEntity<ArrayList<Object>> getAllTransactionSummaryByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NoTransactionForGivenDate {
        log.info("Getting Transaction summary for :" ,date);
        return new ResponseEntity<>(accountService.getAllTransactionSummaryByDate(date),HttpStatus.OK);

    }

}

