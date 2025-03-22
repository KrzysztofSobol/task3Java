package demo.task1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

@Setter
@Getter

@Entity
@Table(name = "ACCOUNT_OPERATIONS")
public class AccountOperation extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "related_account_id")
    private Account relatedAccount;
}