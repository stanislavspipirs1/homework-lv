package io.fourfinanceit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.*;

@Service
public class NightPeriodChecker {

    private final BigDecimal maxLoanAmount;
    private final long nightPeriodEndHours;
    private final long nightPeriodEndMinutes;
    private final Clock clock;

    public NightPeriodChecker(@Value("${loan.amount.max}") BigDecimal maxLoanAmount,
                              @Value("${night.period.end}") String nightPeriodEnd,
                              Clock clock) {
        this.maxLoanAmount = maxLoanAmount;
        this.nightPeriodEndHours = Long.parseLong(nightPeriodEnd.split(":")[0]);
        this.nightPeriodEndMinutes = Long.parseLong(nightPeriodEnd.split(":")[0]);
        this.clock = clock;
    }

    public boolean validate(BigDecimal amount) {
        return amount.compareTo(maxLoanAmount) < 0 || !isNightTime();
    }

    private boolean isNightTime() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime startOfTheDay = now.truncatedTo(DAYS);
        LocalDateTime nightPeriodEnd = startOfTheDay
                .plus(nightPeriodEndHours, HOURS)
                .plus(nightPeriodEndMinutes, MINUTES);
        return now.isBefore(nightPeriodEnd);
    }
}
