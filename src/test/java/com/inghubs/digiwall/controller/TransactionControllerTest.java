package com.inghubs.digiwall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghubs.digiwall.model.dto.ApproveTransactionRequest;
import com.inghubs.digiwall.model.dto.TransactionResponse;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.service.TransactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testListTransactions() throws Exception {
        mockMvc.perform(get("/transaction/list/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testApproveTransaction() throws Exception {
        ApproveTransactionRequest request = new ApproveTransactionRequest();
        request.setTransactionId(1L);
        request.setTransactionStatus(TransactionStatus.APPROVED);

        when(transactionService.approveTransaction(any(ApproveTransactionRequest.class)))
                .thenReturn(new TransactionResponse());

        mockMvc.perform(put("/transaction/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testApproveTransaction_MissingFields() throws Exception {
        ApproveTransactionRequest request = new ApproveTransactionRequest();
        mockMvc.perform(put("/transaction/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}