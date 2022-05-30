package com.example.DotpayInstantPayment.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    @NotBlank(message = "input account name")
    private String accountName;
    private Long accountNumber;
    private BigDecimal accountBalance= BigDecimal.valueOf(0.00);

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Transaction> transactions ;


}
