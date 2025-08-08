package com.inghubs.digiwall.service;

import com.inghubs.digiwall.exception.BadRequestException;
import com.inghubs.digiwall.exception.ResourceNotFoundException;
import com.inghubs.digiwall.model.dto.ApproveTransactionRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.entity.Transaction;
import com.inghubs.digiwall.model.entity.Wallet;
import com.inghubs.digiwall.model.mapper.TransactionMapper;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.model.type.TransactionType;
import com.inghubs.digiwall.repository.TransactionRepository;
import com.inghubs.digiwall.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponse> listTransactionsByWalletId(Long walletId) {
        return transactionRepository.findByWalletId(walletId)
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse approveTransaction(ApproveTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getTransactionStatus() != TransactionStatus.PENDING) {
            throw new BadRequestException("Transaction already processed");
        }

        Wallet wallet = transaction.getWallet();
        BigDecimal amount = transaction.getAmount();

        if (request.getTransactionStatus() == TransactionStatus.APPROVED) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                wallet.setBalance(wallet.getBalance().subtract(amount));
            }
        } else if (request.getTransactionStatus() == TransactionStatus.DENIED) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance().subtract(amount));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
            }
        }

        transaction.setTransactionStatus(request.getTransactionStatus());
        walletRepository.save(wallet);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

}