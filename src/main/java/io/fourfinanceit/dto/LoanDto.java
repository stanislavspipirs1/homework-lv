package io.fourfinanceit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.Loan.LoanStatus;

import java.math.BigDecimal;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public final class LoanDto {

    @Getter private final Long id;
    @Getter private final String username;
    @Getter private final BigDecimal amount;
    @Getter private final long term;
    @Getter private final BigDecimal interestRate;
    @Getter private final LoanStatus status;
    @Getter private final Date createdAt;
    @Getter @JsonInclude(NON_NULL) private Long extensionTerm;
    @Getter @JsonInclude(NON_NULL) private BigDecimal extensionCoefficient;
    @Getter @JsonInclude(NON_NULL) private Date extendedAt;

    public LoanDto(Loan loan) {
        this.id = loan.getId();
        this.username = loan.getUsername();
        this.amount = loan.getAmount();
        this.term = loan.getTerm();
        this.interestRate = loan.getInterestRate();
        this.status = loan.getStatus();
        this.createdAt = loan.getCreatedAt();
        this.extensionTerm = loan.getExtensionTerm();
        this.extensionCoefficient = loan.getExtensionCoefficient();
        this.extendedAt = loan.getExtendedAt();
    }
}