package com.inghubs.digiwall.model.mapper;

import com.inghubs.digiwall.model.dto.DepositRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.dto.WithdrawRequest;
import com.inghubs.digiwall.model.entity.Transaction;
import com.inghubs.digiwall.model.entity.Wallet;
import com.inghubs.digiwall.model.type.OppositePartyType;
import com.inghubs.digiwall.model.type.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toDepositEntity(DepositRequest request, Wallet wallet) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setOppositeParty(request.getSource());
        transaction.setOppositePartyType(OppositePartyType.valueOf(request.getOppositePartyType()));
        return transaction;
    }

    public Transaction toWithdrawEntity(WithdrawRequest request, Wallet wallet) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setOppositeParty(request.getDestination());
        transaction.setOppositePartyType(OppositePartyType.valueOf(request.getOppositePartyType()));
        return transaction;
    }

    public TransactionResponse toDto(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .walletId(transaction.getWallet().getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .oppositeParty(transaction.getOppositeParty())
                .oppositePartyType(transaction.getOppositePartyType())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }
}