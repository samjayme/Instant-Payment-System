package com.example.DotpayInstantPayment.scheduler;

import com.example.DotpayInstantPayment.entity.Transaction;
import com.example.DotpayInstantPayment.entity.TransactionStatus;
import com.example.DotpayInstantPayment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.DotpayInstantPayment.entity.TransactionStatus.*;



@Service
public class DotPayScheduledTaskService {

    @Autowired
    private TransactionRepository transactionRepository;

    public String GetSuccessFullTransactionAndCommissionStatus() {
        List<Transaction> transaction = transactionRepository.findAll();
        for (Transaction t : transaction) {
            if (t.getStatus().equals(SUCCESSFUL)) {
                System.out.println("successFull and CommissionWorthy");
                System.out.println(t);
                System.out.println("Commissions");
                System.out.println(t.getCommission());
            }
        }

        return "completed";
    }

    public String getTransactionSummary() {
        List<Transaction> transactionList = transactionRepository.findAll();

        Map<LocalDate,List<Transaction>> transactions=transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getDate));
        Map<LocalDate,List<BigDecimal>> charges= transactionList.stream().
                collect(Collectors.groupingBy(transaction -> transaction.getDate(),
                        Collectors.mapping(Transaction::getTransactionFee,Collectors.toList())));
        Map<LocalDate,List<BigDecimal>> commissions= transactionList.stream().
                collect(Collectors.groupingBy(transaction -> transaction.getDate(),
                        Collectors.mapping(Transaction::getCommission,Collectors.toList())));
        Map<LocalDate,List<BigDecimal>> amount= transactionList.stream().
                collect(Collectors.groupingBy(transaction -> transaction.getDate(),
                        Collectors.mapping(Transaction::getAmount,Collectors.toList())));
        Map<LocalDate,List<TransactionStatus>> transaction_status= transactionList.stream().
                collect(Collectors.groupingBy(transaction -> transaction.getDate(),
                        Collectors.mapping(transaction -> transaction.getStatus(),Collectors.toList())));

        System.out.println("TRANSACTIONS");
        System.out.println(transactions);
        System.out.println("TRANSACTION STATUS ");
        System.out.println(transaction_status);
        System.out.println("TRANSACTION AMOUNT");
        System.out.println(amount);
        System.out.println("COMMISSIONS");
        System.out.println(commissions);
        System.out.println("CHARGES");
        System.out.println(charges);
        return null;
    }


    }