import org.example.wallet.dto.WalletOperationRequest;
import org.example.wallet.entity.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.example.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testOperateWalletDeposit() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(1000L);

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(walletId);
        request.setOperationType(WalletOperationRequest.OperationType.DEPOSIT);
        request.setAmount(500L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.operateWallet(request);

        verify(walletRepository, times(1)).save(wallet);
        assertEquals(1500L, wallet.getBalance());
    }

    @Test
    @Transactional
    void testOperateWalletWithdraw() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(1000L);

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(walletId);
        request.setOperationType(WalletOperationRequest.OperationType.WITHDRAW);
        request.setAmount(300L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.operateWallet(request);

        verify(walletRepository, times(1)).save(wallet);
        assertEquals(700L, wallet.getBalance());
    }

    @Test
    void testOperateWalletWithdrawInsufficientFunds() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(100L);

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(walletId);
        request.setOperationType(WalletOperationRequest.OperationType.WITHDRAW);
        request.setAmount(200L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            walletService.operateWallet(request);
        });
        assertEquals("Insufficient funds", thrown.getMessage());
    }

    @Test
    void testGetWalletBalance() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(1000L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        long balance = walletService.getWalletBalance(walletId);

        assertEquals(1000L, balance);
    }

    @Test
    void testGetWalletBalanceNotFound() {
        UUID walletId = UUID.randomUUID();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            walletService.getWalletBalance(walletId);
        });
        assertEquals("Wallet not found", thrown.getMessage());
    }
}

