package com.inghubs.digiwall.controller;

import com.inghubs.digiwall.model.dto.ApproveTransactionRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TransactionController extends AbstractController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    @GetMapping(value = LIST_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> listTransactions(@PathVariable Long walletId) {
        return ResponseEntity.ok(transactionService.listTransactionsByWalletId(walletId));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping(value = APPROVE_TRANSACTION)
    public ResponseEntity<TransactionResponse> approveTransaction(@RequestBody @Valid ApproveTransactionRequest request) {
        return ResponseEntity.ok(transactionService.approveTransaction(request));
    }
}