package org.example.wallet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
public class WalletOperationRequest {
    private UUID walletId;
    private OperationType operationType;
    private long amount;

}
