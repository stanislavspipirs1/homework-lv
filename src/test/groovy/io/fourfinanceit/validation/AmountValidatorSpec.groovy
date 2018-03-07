package io.fourfinanceit.validation

import spock.lang.Specification

class AmountValidatorSpec extends Specification {

    def amountValidator = new AmountValidator(1.00, 1000.00)

    def 'when valid amount should return true'() {
        expect:
        amountValidator.validate(amount)

        where:
        amount <<  [ 1.00, 500.00, 1000.00 ]
    }

    def 'when invalid amount should throw exception'() {
        when:
        amountValidator.validate(amount)

        then:
        thrown(IllegalArgumentException)

        where:
        amount << [ -1000000.00, -1.00, 0.00, 0.99, 1000.01, 1000000.00]
    }
}
