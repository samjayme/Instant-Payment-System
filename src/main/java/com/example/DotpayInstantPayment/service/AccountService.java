package com.example.DotpayInstantPayment.service;

import com.example.DotpayInstantPayment.entity.Account;
import com.example.DotpayInstantPayment.entity.Transaction;
import com.example.DotpayInstantPayment.exception.AccountNumberNotFoundException;
import com.example.DotpayInstantPayment.exception.NoTransactionForGivenDate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public interface AccountService {

    Account createAccount(Account account);



    List<Transaction> getAllTransactions();

    Account addMoney(Long accountNumber, BigDecimal amount) throws AccountNumberNotFoundException;


    Account transfer (Long MasterAccount,Long toAccountNumber, BigDecimal amount, String description) throws AccountNumberNotFoundException;

    List<Transaction> getAllTransactionByDate(LocalDate date) throws NoTransactionForGivenDate;
    ArrayList<Object> getAllTransactionSummaryByDate(LocalDate date) throws NoTransactionForGivenDate;


}