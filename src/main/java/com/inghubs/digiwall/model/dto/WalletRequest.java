package com.inghubs.digiwall.model.dto;

import com.inghubs.digiwall.model.type.Currency;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletRequest {

    @ApiModelProperty(value = "Customer ID who owns the wallet", required = true, example = "1")
    @NotNull
    private Long customerId;

    @ApiModelProperty(value = "Wallet name", required = true, example = "Main Wallet")
    @NotNull
    private String walletName;

    @ApiModelProperty(value = "Wallet currency", required = true, allowableValues = "TRY, USD, EUR", example = "TRY")
    @NotNull
    private Currency currency;

    @ApiModelProperty(value = "Can this wallet be used for shopping?", example = "true")
    private boolean activeForShopping;

    @ApiModelProperty(value = "Can this wallet be used for withdrawal?", example = "false")
    private boolean activeForWithdraw;
}