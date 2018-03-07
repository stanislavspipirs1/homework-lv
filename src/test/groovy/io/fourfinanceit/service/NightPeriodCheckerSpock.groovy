package io.fourfinanceit.service

import spock.lang.Specification

import java.time.Clock
import java.time.Instant

import static java.time.LocalDateTime.now
import static java.time.ZoneId.systemDefault
import static java.time.temporal.ChronoUnit.DAYS
import static java.time.temporal.ChronoUnit.HOURS

class NightPeriodCheckerSpock extends Specification {

    def clock2am = Mock Clock
    def clock2Pm = Mock Clock

    def setup() {
        def am2 = now().truncatedTo(DAYS).plus(2, HOURS).atZone(systemDefault()).toInstant().toEpochMilli()
        def pm2 = now().truncatedTo(DAYS).plus(14, HOURS).atZone(systemDefault()).toInstant().toEpochMilli()
        clock2am.instant() >> Instant.ofEpochMilli(am2)
        clock2am.getZone() >> systemDefault()
        clock2Pm.instant() >> Instant.ofEpochMilli(pm2)
        clock2Pm.getZone() >> systemDefault()
    }

    def 'when night time should alert on large loans'() {
        when:
        def nightPeriodChecker = new NightPeriodChecker(1000.00, '5:00', clock2am)

        then:
        nightPeriodChecker.validate(999.00)
        !nightPeriodChecker.validate(1000.00)
        !nightPeriodChecker.validate(1001.00)
    }

    def 'when after night time should not alert on any loan'() {
        when:
        def nightPeriodChecker = new NightPeriodChecker(1000.00, '5:00', clock2Pm)

        then:
        nightPeriodChecker.validate(999.00)
        nightPeriodChecker.validate(1000.00)
        nightPeriodChecker.validate(1001.00)
    }
}
