package io.fourfinanceit.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountValidator implements Validator<BigDecimal> {

    private final BigDecimal minLoanAmount;
    private final BigDecimal maxLoanAmount;

    public AmountValidator(@Value("${loan.amount.min}") BigDecimal minLoanAmount,
                           @Value("${loan.amount.max}") BigDecimal maxLoanAmount) {
        this.minLoanAmount = minLoanAmount;
        this.maxLoanAmount = maxLoanAmount;
    }

    @Override
    public boolean validate(BigDecimal value) {
        if (value.compareTo(minLoanAmount) >= 0 && value.compareTo(maxLoanAmount) <= 0) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid amount " + value);
        }
    }
}
