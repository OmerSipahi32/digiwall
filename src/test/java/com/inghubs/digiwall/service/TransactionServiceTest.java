package com.inghubs.digiwall.service;

import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.entity.Transaction;
import com.inghubs.digiwall.model.entity.Wallet;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListTransactionsByWalletId() {
        List<Transaction> mockList = new ArrayList<>();
        Transaction t1 = new Transaction();
        t1.setAmount(BigDecimal.valueOf(1000));
        mockList.add(t1);

        when(transactionRepository.findByWalletId(1L)).thenReturn(mockList);

        List<TransactionResponse> result = transactionService.listTransactionsByWalletId(1L);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByWalletId(1L);
    }

    @Test
    void testDepositTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(500));
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        transaction.setWallet(wallet);
        transaction.setTransactionStatus(TransactionStatus.APPROVED);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
    }

    @Test
    void testApproveTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
    }

    @Test
    void testApproveTransaction_NotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());
    }
}