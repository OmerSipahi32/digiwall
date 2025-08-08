package com.inghubs.digiwall.model.entity;

import com.inghubs.digiwall.model.base.ExtendedModel;
import com.inghubs.digiwall.model.type.OppositePartyType;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.model.type.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Transaction extends ExtendedModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType;

    private String oppositeParty;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}