package com.inghubs.digiwall.model.entity;

import com.inghubs.digiwall.model.base.ExtendedModel;
import com.inghubs.digiwall.model.type.Currency;
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
public class Wallet extends ExtendedModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    private String walletName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private boolean activeForShopping;
    private boolean activeForWithdraw;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal usableBalance = BigDecimal.ZERO;
}