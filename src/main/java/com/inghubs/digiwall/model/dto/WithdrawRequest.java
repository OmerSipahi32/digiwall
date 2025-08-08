package com.inghubs.digiwall.model.dto;

import com.inghubs.digiwall.model.util.ValidIban;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    @ApiModelProperty(value = "Source Wallet ID", required = true, example = "5")
    @NotNull
    private Long walletId;

    @ApiModelProperty(value = "Amount to withdraw", required = true, example = "250.00")
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @ApiModelProperty(value = "Destination IBAN or Payment ID", required = true, example = "TR330006100519786457841326")
    @ValidIban
    private String destination;

    @ApiModelProperty(value = "Opposite party type (IBAN or PAYMENT)", required = true, allowableValues = "IBAN, PAYMENT", example = "IBAN")
    @NotBlank
    private String oppositePartyType;
}