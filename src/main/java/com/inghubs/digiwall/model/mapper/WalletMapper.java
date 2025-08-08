package com.inghubs.digiwall.model.mapper;

import com.inghubs.digiwall.model.dto.WalletRequest;
import com.inghubs.digiwall.model.dto.WalletResponse;
import com.inghubs.digiwall.model.entity.Customer;
import com.inghubs.digiwall.model.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletMapper {

    public Wallet toEntity(WalletRequest request, Customer customer) {
        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setWalletName(request.getWalletName());
        wallet.setCurrency(request.getCurrency());
        wallet.setActiveForShopping(request.isActiveForShopping());
        wallet.setActiveForWithdraw(request.isActiveForWithdraw());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);
        return wallet;
    }

    public WalletResponse toDto(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .customerId(wallet.getCustomer().getId())
                .walletName(wallet.getWalletName())
                .currency(wallet.getCurrency())
                .activeForShopping(wallet.isActiveForShopping())
                .activeForWithdraw(wallet.isActiveForWithdraw())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .build();
    }
}