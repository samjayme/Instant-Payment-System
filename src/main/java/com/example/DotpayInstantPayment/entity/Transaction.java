package com.example.DotpayInstantPayment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private BigDecimal amount;
    private BigDecimal transactionFee;
    private BigDecimal billedAmount ;
    private String referenceNumber = RandomStringUtils.randomNumeric(15);
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date ;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private boolean commissionWorthy;
    private BigDecimal commission;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "transactions")
    private List<Account> account;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return isCommissionWorthy() == that.isCommissionWorthy() && Objects.equals(getTransactionId(), that.getTransactionId()) && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getTransactionFee(), that.getTransactionFee()) && Objects.equals(getBilledAmount(), that.getBilledAmount()) && Objects.equals(getReferenceNumber(), that.getReferenceNumber()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getDate(), that.getDate()) && getStatus() == that.getStatus() && Objects.equals(getCommission(), that.getCommission()) && Objects.equals(getAccount(), that.getAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionId(), getAmount(), getTransactionFee(), getBilledAmount(), getReferenceNumber(), getDescription(), getDate(), getStatus(), isCommissionWorthy(), getCommission(), getAccount());
    }
}
