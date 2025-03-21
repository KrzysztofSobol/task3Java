package demo.task1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

@Setter
@Getter

@Entity
@Table(name = "ACCOUNTS")
public class Account extends AbstractModel{
    private String name;
    private String address;
    private BigDecimal balance = new BigDecimal(0);

    public Account(Account account) {
        this.name = account.getName();
        this.address = account.getAddress();
        this.balance = account.getBalance();
    }
}
