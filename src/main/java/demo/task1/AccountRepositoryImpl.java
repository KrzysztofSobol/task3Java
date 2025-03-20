package demo.task1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {
    private ArrayList<Account> accounts = new ArrayList<>();
    private long idgen;

    private Long nextId() {
        return idgen++;
    }

    @Override
    public Account create(String name, String address, BigDecimal balance) {
        Account a = new Account(nextId(), name, address, balance);
        accounts.add(a);
        return new Account(a);
    }

    // fix 2: not using findById because it returns a copy
    // didn't change the findById impl, so I don't break the encapsulation
    // moved the null check up so its similar to findById impl
    @Override
    public void save(Account account) {
        if (account.getId() == null){
            throw new IllegalArgumentException();
        }

        Account foundAccount = accounts.stream()
                .filter(acc -> Objects.equals(acc.getId(), account.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        foundAccount.setName(account.getName());
        foundAccount.setAddress(account.getAddress());
        foundAccount.setBalance(account.getBalance());
    }

    // fix 1: throw exception when null
    @Override
    public Optional<Account> findById(Long id) {
        if(id == null){
            throw new IllegalArgumentException();
        }

        return accounts.stream()
                .filter(account -> id.equals(account.getId()))
                .findFirst()
                .map(Account::new); // shortened version of lambda
    }

    @Override
    public Optional<Account> findByNameAndAddress(String name, String address) {
        return accounts.stream().filter(a->a.getName().equals(name) && a.getAddress().equals(address)).findFirst();
    }
}
