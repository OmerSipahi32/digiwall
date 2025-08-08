package com.inghubs.digiwall.model.dto;

import com.inghubs.digiwall.model.type.OppositePartyType;
import com.inghubs.digiwall.model.type.TransactionStatus;
import com.inghubs.digiwall.model.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private TransactionType type;
    private OppositePartyType oppositePartyType;
    private String oppositeParty;
    private TransactionStatus transactionStatus;

    private LocalDateTime createdAt;
}