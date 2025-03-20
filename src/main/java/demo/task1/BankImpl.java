package demo.task1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class BankImpl implements Bank {

    private AccountRepository accountRepository;
    private static final Logger logger = Logger.getLogger(BankImpl.class.getName());

    public BankImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        logger.info("Back instance created with repository: " + accountRepository.getClass().getName());
    }

    @Override
    public Long createAccount(String name, String address) {
        logger.fine("Creating account for: " + name + " with address: " + address);

        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        if (account.isPresent()) { return account.get().getId(); }
        Long id = accountRepository.create(name,address,BigDecimal.ZERO).getId();

        logger.finer("Account created successfully with ID: " + id);
        return id;
    }

    @Override
    public Long findAccount(String name, String address) {
        logger.fine("Finding account for: " + name + " with address: " + address);

        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);

        if(account.isPresent()){
            logger.finer("Account found: " + account.get().getId());
            return account.get().getId();
        } else {
            logger.finer("Account not found!");
            return null;
        }
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        try {
            logger.fine("Making a deposit for" + id + " with amount: " + amount);
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                logger.severe("Account with id: " + id + " not found for the deposit!");
                throw new AccountIdException();
            }

            Account ac = account.get();

            if(amount == null){
                logger.finest("Deposit amount is null, defaulting to ZERO");
                amount = BigDecimal.ZERO;
            }

            ac.setBalance(ac.getBalance().add(amount));

            logger.finer("Deposit successful for account: " + ac.getId());
            accountRepository.save(ac);
        } catch(IllegalArgumentException e) {
            logger.severe("Account with id: " + id + " not found for the deposit!");
            throw new AccountIdException();
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {
        try {
            logger.fine("Getting balance for " + id);
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                logger.severe("Account with id: " + id + " not found!");
                throw new AccountIdException();
            }

            BigDecimal balance = account.get().getBalance();
            logger.finer("Got balance: " + balance + " for account: " + account.get().getId());
            return balance;
        } catch (IllegalArgumentException e) {
            logger.severe("Account with id: " + id + " not found!");
            throw new AccountIdException();
        }
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        try {
            logger.fine("Making a withdrawal for " + id + " with amount: " + amount);
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                logger.severe("Account with id: " + id + " not found for the withdraw!");
                throw new AccountIdException();
            }

            Account ac = account.get();
            BigDecimal currentBalance = ac.getBalance();

            if(amount == null){
                logger.finest("Withdraw amount is null, defaulting to ZERO");
                amount = BigDecimal.ZERO;
            }

            if(currentBalance.compareTo(amount) < 0) {
                logger.severe("Withdraw amount is insufficient for account: " + ac.getId());
                throw new InsufficientFundsException();
            }

            BigDecimal newBalance = currentBalance.subtract(amount);
            ac.setBalance(newBalance);

            logger.finer("Withdraw successful for account: " + ac.getId());
            logger.finer("Current balance: " + newBalance + " for account: " + ac.getId());
            accountRepository.save(ac);
        } catch (IllegalArgumentException e) {
            logger.severe("Account with id: " + id + " not found for the withdraw!");
            throw new AccountIdException();
        }
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        try {
            logger.fine("Making a transfer from " + idSource + " to " + idDestination);
            Optional<Account> sourceAccount = accountRepository.findById(idSource);
            Optional<Account> destinationAccount = accountRepository.findById(idDestination);

            if(sourceAccount.isEmpty() || destinationAccount.isEmpty()) {
                logger.severe("Account with id: " + idSource + " or " + idDestination + " not found!");
                throw new AccountIdException();
            }

            Account sourceAc = sourceAccount.get();
            Account destAc = destinationAccount.get();

            if(sourceAc.getBalance().compareTo(amount) < 0) {
                logger.severe("Insufficient funds for account: " + sourceAc.getId());
                throw new InsufficientFundsException();
            }

            sourceAc.setBalance(sourceAc.getBalance().subtract(amount));
            destAc.setBalance(destAc.getBalance().add(amount));

            accountRepository.save(sourceAc);
            accountRepository.save(destAc);

            logger.finer("Transfer successful for account: " + sourceAc.getId());
            logger.finer("Current balance of source: " + sourceAc.getBalance() + " after transfering: " + amount + " to destination: " + destAc.getBalance());
        } catch (IllegalArgumentException e) {
            logger.severe("Account with id: " + idSource + " or " + idDestination + " is invalid!");
            throw new AccountIdException();
        }
    }
}
