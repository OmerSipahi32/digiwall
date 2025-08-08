package com.inghubs.digiwall.model.dto;

import com.inghubs.digiwall.model.type.TransactionStatus;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveTransactionRequest {

    @ApiModelProperty(value = "Transaction ID to approve/deny", required = true, example = "100")
    @NotNull
    private Long transactionId;

    @ApiModelProperty(value = "New status of the transaction", required = true, allowableValues = "APPROVED, DENIED", example = "APPROVED")
    @NotNull
    private TransactionStatus transactionStatus;
}