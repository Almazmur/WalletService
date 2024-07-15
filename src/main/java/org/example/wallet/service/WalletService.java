package org.example.wallet.service;

import jakarta.transaction.Transactional;
import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletOperationRequest;
import org.example.wallet.entity.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void operateWallet(WalletOperationRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (OperationType.DEPOSIT.equals(request.getOperationType())) {
            wallet.setBalance(wallet.getBalance() + request.getAmount());
        } else if (OperationType.WITHDRAW.equals(request.getOperationType())) {
            if (wallet.getBalance() < request.getAmount()) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
        } else {
            throw new IllegalArgumentException("Invalid operation type");
        }
        walletRepository.save(wallet);
    }

    public long getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"))
                .getBalance();
    }
}
