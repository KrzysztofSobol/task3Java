package demo.task1.dao;

import demo.task1.models.Account;

import java.math.BigDecimal;

public interface AccountRepository extends GenericDao<Account, Long> {
    Account create(String name, String address, BigDecimal balance);
}
