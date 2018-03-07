package io.fourfinanceit.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@EqualsAndHashCode(of = {"id"})
public final class Loan {

    public enum LoanStatus {
        RISK_ANALYSIS, APPROVED
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Getter private Long id;

    @Getter private String username;
    @Getter private BigDecimal amount;
    @Getter private long term;
    @Getter private BigDecimal interestRate;
    @Getter private LoanStatus status;
    @Getter private Date createdAt = new Date();

    @Getter private Long extensionTerm;
    @Getter private BigDecimal extensionCoefficient;
    @Getter private Date extendedAt;

    public Loan() {
    }

    public Loan(String username, BigDecimal amount, long term, BigDecimal interestRate, LoanStatus status) {
        this.username = username;
        this.amount = amount;
        this.term = term;
        this.interestRate = interestRate;
        this.status = status;
    }

    public void extend(long extensionTerm, BigDecimal extensionCoefficient) {
        this.extensionTerm = extensionTerm;
        this.extensionCoefficient = extensionCoefficient;
        this.extendedAt = new Date();
    }
}