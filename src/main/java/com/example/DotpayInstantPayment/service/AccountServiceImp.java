package com.example.DotpayInstantPayment.service;

import com.example.DotpayInstantPayment.entity.Account;
import com.example.DotpayInstantPayment.entity.Transaction;
import com.example.DotpayInstantPayment.exception.AccountNumberNotFoundException;
import com.example.DotpayInstantPayment.exception.NoTransactionForGivenDate;
import com.example.DotpayInstantPayment.repository.AccountRepository;
import com.example.DotpayInstantPayment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Stream;

import static com.example.DotpayInstantPayment.entity.TransactionStatus.*;

@Component
public class AccountServiceImp implements AccountService {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account createAccount(Account account) {

        return accountRepository.save(account);
    }


    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Account addMoney(Long accountNumber, BigDecimal amount) throws AccountNumberNotFoundException {
        Account account = accountRepository.findAll().stream().filter(a -> a.getAccountNumber().equals(accountNumber))
                .findAny().orElseThrow(() -> new
                        AccountNumberNotFoundException("Invalid account number"));


        BigDecimal accountBalance = account.getAccountBalance();
        account.setAccountBalance(accountBalance.add(amount));
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account transfer(Long MasterAccount, Long toAccountNumber, BigDecimal amount, String description) throws AccountNumberNotFoundException {

        Transaction transaction = new Transaction();
        BigDecimal transactionFee = amount.multiply(BigDecimal.valueOf(0.5));
        BigDecimal commission = transactionFee.multiply(BigDecimal.valueOf(0.2));
        BigDecimal billedAmount = transactionFee.add(amount);

       Account depositorsAccount = accountRepository.findAll().stream().filter(account -> account.getAccountNumber()
                .equals(MasterAccount)).findAny().orElseThrow(() -> new
                AccountNumberNotFoundException("Invalid depositorsAccount account number "));
        Account recipientAccount = accountRepository.findAll().stream()
                .filter(account -> account.getAccountNumber().equals(toAccountNumber)).findAny().orElseThrow(() -> new
                        AccountNumberNotFoundException("Invalid recipientAccount account number"));
        int transferCondition;
        transferCondition = depositorsAccount.getAccountBalance().compareTo(amount);
        if (transferCondition == -1) {
            transaction.setStatus(INSUFFICIENT_FUND);
            transaction.setDescription(description);
            transaction.setCommissionWorthy(false);

        } else if (transferCondition == 0 || transferCondition == 1) {

            depositorsAccount.setAccountBalance((depositorsAccount.getAccountBalance().subtract(billedAmount)));
            recipientAccount.setAccountBalance(recipientAccount.getAccountBalance().add(amount));
            transaction.setTransactionFee(transactionFee);
            transaction.setBilledAmount(billedAmount);
            transaction.setStatus(SUCCESSFUL);
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setCommissionWorthy(true);
            transaction.setCommission(commission);
            transaction.setDate(LocalDate.now());
            accountRepository.save(depositorsAccount);
            accountRepository.save(recipientAccount);
            transactionRepository.save(transaction);
        } else {
            transaction.setStatus(DECLINED);
            transaction.setCommissionWorthy(false);
        }

        return depositorsAccount;
    }

    @Override
    public List<Transaction> getAllTransactionByDate(LocalDate date) throws NoTransactionForGivenDate {
        List<Transaction> transactions ;
        if (date == null) {
            throw new NoTransactionForGivenDate("date is empty");
        } else if (date.isSupported(ChronoField.YEAR_OF_ERA)) {
            transactions = transactionRepository.findByDate(date).stream().toList();
            if (transactions.isEmpty()){
                throw  new NoTransactionForGivenDate("No transaction for date " +date);
            }
        } else {
            throw new NoTransactionForGivenDate("No transactions for given date" + date);
        }
        return transactions;
    }

    @Override
    public ArrayList<Object> getAllTransactionSummaryByDate(LocalDate date) throws NoTransactionForGivenDate {
        ArrayList<Object> transactionSummary = new ArrayList<>();

        List<Transaction> transactions = transactionRepository.findByDate(date);
            BigDecimal totalTransactionCharges = transactions.stream().map(transaction -> transaction.getTransactionFee()).
                    reduce((BigDecimal::add)).get();
            BigDecimal totalTransactionAmount = transactions.stream().map(transaction -> transaction.getAmount()).
                    reduce((BigDecimal::add)).get();
            BigDecimal totalCommission = transactions.stream().map(transaction -> transaction.getCommission()).
                    reduce((BigDecimal::add)).get();
            List<Transaction> SuccessFulTransactions = transactions.stream().filter(transaction -> transaction.getStatus().
                    equals(SUCCESSFUL)).toList();
            int TotalSuccessFullTransactions = SuccessFulTransactions.size();
            List<Transaction> InsufficientFundTransactions = transactions.stream().filter(transaction -> transaction.getStatus().
                    equals(INSUFFICIENT_FUND)).toList();
            int TotalInsufficientFundTransactions = InsufficientFundTransactions.size();
            List<Transaction> DeclinedTransactions = transactions.stream().filter(transaction -> transaction.getStatus().
                    equals(DECLINED)).toList();
            int TotalDeclinedTransactions = DeclinedTransactions.size();

        transactionSummary.add(date);
        transactionSummary.add("TOTAL SUCCESSFUL TRANSACTIONS");
        transactionSummary.add(TotalSuccessFullTransactions);
        transactionSummary.add("TOTAL INSUFFICIENT_FUND TRANSACTIONS");
        transactionSummary.add(TotalInsufficientFundTransactions);
        transactionSummary.add(" TOTAL DECLINED TRANSACTIONS");
        transactionSummary.add(TotalDeclinedTransactions);
        transactionSummary.add("TOTAL TRANSACTION_CHARGES ");
        transactionSummary.add(totalTransactionCharges);
        transactionSummary.add("TOTAL TRANSACTION_AMOUNT ");
        transactionSummary.add(totalTransactionAmount);
        transactionSummary.add("TOTAL COMMISSIONS");
        transactionSummary.add(totalCommission);


        return transactionSummary;
    }
}