package com.inghubs.digiwall.service;

import com.inghubs.digiwall.exception.ResourceNotFoundException;
import com.inghubs.digiwall.model.dto.WalletRequest;
import com.inghubs.digiwall.model.dto.WalletResponse;
import com.inghubs.digiwall.model.entity.Wallet;
import com.inghubs.digiwall.model.type.Currency;
import com.inghubs.digiwall.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWallet_Success() {
        WalletRequest request = new WalletRequest();
        request.setWalletName("My Wallet");
        request.setCurrency(Currency.USD);
        request.setActiveForShopping(true);
        request.setActiveForWithdraw(false);

        Wallet savedWallet = new Wallet();
        savedWallet.setWalletName(request.getWalletName());
        savedWallet.setCurrency(request.getCurrency());
        savedWallet.setActiveForShopping(request.isActiveForShopping());
        savedWallet.setActiveForWithdraw(request.isActiveForWithdraw());
        savedWallet.setBalance(BigDecimal.ZERO);
        savedWallet.setUsableBalance(BigDecimal.ZERO);

        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);

        WalletResponse result = walletService.createWallet(request);

        assertNotNull(result);
        assertEquals("My Wallet", result.getWalletName());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testGetWalletById_Found() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setWalletName("Test Wallet");

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        Wallet found = walletService.getWalletById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test Wallet", found.getWalletName());
    }

    @Test
    void testGetWalletById_NotFound() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> walletService.getWalletById(1L));

        assertEquals("Wallet not found with id: 1", ex.getMessage());
    }

    @Test
    void testListWalletsByCustomerId() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setWalletName("Test Wallet");
        wallet.setCurrency(Currency.TRY);
        wallet.setActiveForShopping(true);
        wallet.setActiveForWithdraw(true);
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setUsableBalance(BigDecimal.valueOf(800));

        when(walletRepository.findByCustomerId(1L)).thenReturn(List.of(wallet));

        var result = walletService.listWalletsByCustomerId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Wallet", result.getFirst().getWalletName());
    }

    @Test
    void testCreateWallet_InvalidCurrency() {
        WalletRequest request = new WalletRequest();
        request.setWalletName("Invalid");
        request.setActiveForShopping(true);
        request.setActiveForWithdraw(true);
        assertThrows(NullPointerException.class, () -> walletService.createWallet(request));
    }
}