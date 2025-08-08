package com.inghubs.digiwall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghubs.digiwall.exception.GlobalExceptionHandler;
import com.inghubs.digiwall.model.dto.DepositRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.dto.WalletRequest;
import com.inghubs.digiwall.model.dto.WalletResponse;
import com.inghubs.digiwall.model.dto.WithdrawRequest;
import com.inghubs.digiwall.model.type.Currency;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(walletController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetWallets() throws Exception {
        mockMvc.perform(get("/wallet/list")
                        .param("customerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateWallet() throws Exception {
        WalletRequest request = new WalletRequest();
        request.setCustomerId(1L);
        request.setWalletName("Test Wallet");
        request.setCurrency(Currency.TRY);
        request.setActiveForShopping(true);
        request.setActiveForWithdraw(true);

        WalletResponse mockResponse = new WalletResponse();
        mockResponse.setId(1L);
        mockResponse.setWalletName("Test Wallet");

        when(walletService.createWallet(any(WalletRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/wallet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeposit() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setWalletId(1L);
        request.setAmount(BigDecimal.valueOf(500));
        request.setSource("TR000000000000000000000000");

        TransactionResponse mockResponse = new TransactionResponse();
        mockResponse.setId(456L);
        mockResponse.setTransactionStatus(TransactionStatus.APPROVED);
        when(walletService.deposit(any(DepositRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testWithdraw() throws Exception {
        WithdrawRequest request = new WithdrawRequest();
        request.setWalletId(1L);
        request.setAmount(BigDecimal.valueOf(200));
        request.setDestination("TR000000000000000000000000");

        TransactionResponse mockResponse = new TransactionResponse();
        mockResponse.setId(123L);
        mockResponse.setTransactionStatus(TransactionStatus.APPROVED);

        when(walletService.withdraw(any(WithdrawRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateWallet_MissingFields() throws Exception {
        WalletRequest request = new WalletRequest();

        mockMvc.perform(post("/wallet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeposit_InvalidAmount() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setWalletId(1L);
        request.setAmount(BigDecimal.valueOf(-100));
        request.setSource("TR000000000000000000000000");

        when(walletService.deposit(any(DepositRequest.class)))
                .thenThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(post("/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}