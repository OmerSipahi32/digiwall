package com.inghubs.digiwall.model.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {

    @ApiModelProperty(value = "Target Wallet ID", required = true, example = "5")
    @NotNull
    private Long walletId;

    @ApiModelProperty(value = "Amount to deposit", required = true, example = "500.00")
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @ApiModelProperty(value = "Source of deposit (IBAN or Payment ID)", required = true, example = "TR330006100519786457841326")
    @NotBlank
    private String source;

    @ApiModelProperty(value = "Opposite party type (IBAN or PAYMENT)", required = true, allowableValues = "IBAN, PAYMENT", example = "IBAN")
    @NotBlank
    private String oppositePartyType;
}