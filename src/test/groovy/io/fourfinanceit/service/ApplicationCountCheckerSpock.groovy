package io.fourfinanceit.service

import spock.lang.Specification

import java.time.Clock

class ApplicationCountCheckerSpock extends Specification {

    def clock = Mock Clock
    def applicationCountChecker = new ApplicationCountChecker(2, clock)

    def now = System.currentTimeMillis()
    def twoHoursAgo = System.currentTimeMillis() - 2 * 60 * 60 * 1000
    def twoDaysAgo = System.currentTimeMillis() - 48 * 60 * 60 * 1000

    def 'when max application limit exceeded should alert'() {
        expect:
        applicationCountChecker.validate('1.2.3.4')
        applicationCountChecker.validate('1.2.3.4')
        !applicationCountChecker.validate('1.2.3.4')
    }

    def 'when different ip should not alert'() {
        expect:
        applicationCountChecker.validate('1.2.3.4')
        applicationCountChecker.validate('1.2.3.4')
        !applicationCountChecker.validate('1.2.3.4')
        applicationCountChecker.validate('4.3.2.1')
    }

    def 'when previous applications expired should not alert'() {
        given:
        clock.millis() >>> [ twoDaysAgo, now, twoHoursAgo, now, now, now ]

        expect:
        applicationCountChecker.validate('1.2.3.4')
        applicationCountChecker.validate('1.2.3.4')
        applicationCountChecker.validate('1.2.3.4')
    }
}
