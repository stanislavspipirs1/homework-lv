package io.fourfinanceit.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TermValidator implements Validator<Long> {

    private final long minLoanTerm;
    private final long maxLoanTerm;

    public TermValidator(@Value("${loan.term.min}") long minLoanTerm,
                         @Value("${loan.term.max}") long maxLoanTerm) {
        this.minLoanTerm = minLoanTerm;
        this.maxLoanTerm = maxLoanTerm;
    }

    @Override
    public boolean validate(Long value) {
        if (value >= minLoanTerm && value <= maxLoanTerm) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid term " + value);
        }
    }
}
