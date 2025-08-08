package com.inghubs.digiwall.controller;

import com.inghubs.digiwall.model.dto.DepositRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.dto.WalletRequest;
import com.inghubs.digiwall.model.dto.WalletResponse;
import com.inghubs.digiwall.model.dto.WithdrawRequest;
import com.inghubs.digiwall.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WalletController extends AbstractController {

    private final WalletService walletService;

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    @PostMapping(value = CREATE_WALLET)
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid WalletRequest request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    @GetMapping(value = LIST_WALLETS)
    public ResponseEntity<List<WalletResponse>> listWallets(@RequestParam Long customerId) {
        return ResponseEntity.ok(walletService.listWalletsByCustomerId(customerId));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    @PostMapping(value = DEPOSIT)
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(request));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    @PostMapping(value = WITHDRAW)
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request));
    }
}
