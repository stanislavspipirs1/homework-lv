package io.fourfinanceit.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import io.fourfinanceit.domain.Loan;
import org.springframework.stereotype.Service;

import io.fourfinanceit.domain.Loan.LoanStatus;
import io.fourfinanceit.dto.LoanDto;
import io.fourfinanceit.repository.LoanRepository;

import static java.util.stream.Collectors.toList;
import static io.fourfinanceit.domain.Loan.LoanStatus.APPROVED;
import static io.fourfinanceit.domain.Loan.LoanStatus.RISK_ANALYSIS;

@Service
public class LoanService {

    private final LoanRepository repository;
    private final ApplicationCountChecker applicationCountChecker;
    private final NightPeriodChecker nightPeriodChecker;
    private final BigDecimal interestRate;
    private final BigDecimal extensionCoefficient;

    public LoanService(LoanRepository repository,
                       ApplicationCountChecker applicationCountChecker,
                       NightPeriodChecker nightPeriodChecker,
                       @Value("${interest.rate}") BigDecimal interestRate,
                       @Value("${interest.rate.extension.coefficient}") BigDecimal extensionCoefficient) {
        this.repository = repository;
        this.applicationCountChecker = applicationCountChecker;
        this.nightPeriodChecker = nightPeriodChecker;
        this.interestRate = interestRate;
        this.extensionCoefficient = extensionCoefficient;
    }

    public LoanDto create(String username, BigDecimal amount, long term, String ip) {
        boolean riskAnalysisNeeded = !applicationCountChecker.validate(ip)
                || !nightPeriodChecker.validate(amount);
        LoanStatus status = riskAnalysisNeeded ? RISK_ANALYSIS : APPROVED;

        Loan loan = new Loan(username, amount, term, interestRate, status);
        repository.save(loan);
        return new LoanDto(loan);
    }

    public LoanDto extend(String username, long id, long term) {
        Loan loan = getLoanToBeExtended(username, id);
        loan.extend(term, extensionCoefficient);
        repository.save(loan);
        return new LoanDto(loan);
    }

    private Loan getLoanToBeExtended(String username, long id) {
        Loan loan = repository.findOneByIdAndUsername(id, username);
        if (loan == null) {
            throw new IllegalArgumentException("Can't find loan by id " + id + " and user " + username);
        }
        if (loan.getExtendedAt() != null) {
            throw new IllegalArgumentException("Loan " + id + " already extended");
        }
        return loan;
    }

    public List<LoanDto> getAll(String username) {
        return repository.findByUsernameOrderByCreatedAtAsc(username).stream().map(LoanDto::new).collect(toList());
    }
}