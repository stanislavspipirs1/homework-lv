package io.fourfinanceit.validation

import spock.lang.Specification

class TermValidatorSpec extends Specification {

    def termValidator = new TermValidator(1, 365)

    def 'when valid term should return true'() {
        expect:
        termValidator.validate(term)

        where:
        term <<  [ 1, 100, 365 ]
    }

    def 'when invalid amount should throw exception'() {
        when:
        termValidator.validate(term)

        then:
        thrown(IllegalArgumentException)

        where:
        term << [ -1000000, -1, 0, 366, 1000000]
    }
}
