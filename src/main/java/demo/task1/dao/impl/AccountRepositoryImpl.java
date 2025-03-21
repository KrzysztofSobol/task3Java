package demo.task1.dao.impl;

import demo.task1.models.Account;
import demo.task1.dao.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl extends GenericDaoImpl<Account, Long> implements AccountRepository {

    @Override
    public Account create(String name, String address, BigDecimal balance) {
        Account account = new Account(name, address, balance);
        save(account);
        return account;
    }
}
