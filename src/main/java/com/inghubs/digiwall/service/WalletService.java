package com.inghubs.digiwall.service;

import com.inghubs.digiwall.exception.BadRequestException;
import com.inghubs.digiwall.exception.ResourceNotFoundException;
import com.inghubs.digiwall.model.dto.DepositRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.dto.WalletRequest;
import com.inghubs.digiwall.model.dto.WalletResponse;
import com.inghubs.digiwall.model.dto.WithdrawRequest;
import com.inghubs.digiwall.model.entity.Customer;
import com.inghubs.digiwall.model.entity.Transaction;
import com.inghubs.digiwall.model.entity.Wallet;
import com.inghubs.digiwall.model.mapper.TransactionMapper;
import com.inghubs.digiwall.model.mapper.WalletMapper;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.repository.CustomerRepository;
import com.inghubs.digiwall.repository.TransactionRepository;
import com.inghubs.digiwall.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final WalletMapper walletMapper;
    private final TransactionMapper transactionMapper;

    @Transactional
    public WalletResponse createWallet(WalletRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Wallet wallet = walletMapper.toEntity(request, customer);
        walletRepository.save(wallet);
        return walletMapper.toDto(wallet);
    }

    @Transactional(readOnly = true)
    public List<WalletResponse> listWalletsByCustomerId(Long customerId) {
        if (!SecurityUtil.isEmployee()) {
            Long currentCustomerId = SecurityUtil.getCurrentCustomerId();
            if (!customerId.equals(currentCustomerId)) {
                throw new AccessDeniedException("You are not allowed to access another customer's wallets");
            }
        }

        return walletRepository.findByCustomerId(customerId)
                .stream()
                .map(walletMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + walletId));
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Long currentCustomerId = SecurityUtil.getCurrentCustomerId();

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (!SecurityUtil.isEmployee() && !wallet.getCustomer().getId().equals(currentCustomerId)) {
            throw new AccessDeniedException("You can only deposit to your own wallets");
        }

        Transaction transaction = transactionMapper.toDepositEntity(request, wallet);

        if (request.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        } else {
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            wallet.setUsableBalance(wallet.getUsableBalance().add(request.getAmount()));
        }

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (!wallet.isActiveForWithdraw() && request.getOppositePartyType().equals("IBAN")) {
            throw new BadRequestException("This wallet cannot be used for withdraw");
        }

        if (!wallet.isActiveForShopping() && request.getOppositePartyType().equals("PAYMENT")) {
            throw new BadRequestException("This wallet cannot be used for shopping");
        }

        if (wallet.getUsableBalance().compareTo(request.getAmount()) < 0) {
            throw new BadRequestException("Insufficient usable balance");
        }

        Transaction transaction = transactionMapper.toWithdrawEntity(request, wallet);

        if (request.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(request.getAmount()));
        } else {
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(request.getAmount()));
        }

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
        return transactionMapper.toDto(transaction);
    }

}