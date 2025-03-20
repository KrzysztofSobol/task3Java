package demo.task1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BankTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankImpl bank;

    @Test
    void test_create_account() {
        when(accountRepository.create("x","y", BigDecimal.ZERO)).thenReturn(new Account(1L,"x","y", BigDecimal.ZERO));
        when(accountRepository.findByNameAndAddress(anyString(),anyString())).thenReturn(Optional.empty());

        Long id = bank.createAccount("x","y");
        assert id != null;

        verify(accountRepository).create("x","y", BigDecimal.ZERO);
        verify(accountRepository).findByNameAndAddress(anyString(),anyString());
    }

    @Test
    void test_findAccount() {
        when(accountRepository.findByNameAndAddress("x", "y"))
                .thenReturn(Optional.of(new Account(1L, "x", "y", BigDecimal.ZERO)));

        Long foundId = bank.findAccount("x", "y");

        assertEquals(1L, foundId);
        verify(accountRepository).findByNameAndAddress("x", "y");
    }

    @Test
    void test_findAccount_if_account_doesnt_exists() {
        when(accountRepository.findByNameAndAddress("x", "y")).thenReturn(Optional.empty());

        Long foundId = bank.findAccount("x", "y");

        assertNull(foundId);
        verify(accountRepository).findByNameAndAddress("x", "y");
    }

    @Test
    void test_deposit() {
        Account account = new Account(1L, "x", "y", BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        bank.deposit(1L, BigDecimal.valueOf(1));

        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void test_getBalance() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(new Account(1L, "x", "y", BigDecimal.ONE)));

        BigDecimal result = bank.getBalance(1L);

        assertEquals(BigDecimal.ONE, result);
        verify(accountRepository).findById(1L);
    }

    @Test
    void test_getBalance_with_no_previous_deposits() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(new Account(1L, "x", "y", BigDecimal.ZERO)));

        BigDecimal balance = bank.getBalance(1L);

        assertEquals(BigDecimal.ZERO, balance);
        verify(accountRepository).findById(1L);
    }

    @Test
    void test_withdraw() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(new Account(1L, "x", "y", BigDecimal.ONE)));

        bank.withdraw(1L, BigDecimal.ONE);

        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void test_withdraw_null() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(new Account(1L, "x", "y", BigDecimal.ZERO)));

        bank.withdraw(1L, null);

        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void test_transfer() {
        when(accountRepository.findById(1L)).thenReturn(
                Optional.of(new Account(1L, "x", "y", BigDecimal.ONE)));
        when(accountRepository.findById(2L)).thenReturn(
                Optional.of(new Account(2L, "a", "b", BigDecimal.ZERO)));

        bank.transfer(1L, 2L, BigDecimal.ONE);

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(accountRepository, times(2)).save(any(Account.class));
    }
}
