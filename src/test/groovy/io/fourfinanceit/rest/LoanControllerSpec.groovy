package io.fourfinanceit.rest

import io.fourfinanceit.service.LoanService
import io.fourfinanceit.validation.AmountValidator
import io.fourfinanceit.validation.TermValidator
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class LoanControllerSpec extends Specification {

    def amountValidator = Mock AmountValidator
    def termValidator = Mock TermValidator
    def loanService = Mock LoanService
    def httpRequest = Mock HttpServletRequest
    def loanController = new LoanController(amountValidator, termValidator, loanService)

    def 'should create new loan'() {
        when:
        loanController.create('johndoe', 123.45, 30, httpRequest)

        then:
        1 * amountValidator.validate(123.45) >> true
        1 * termValidator.validate(30) >> true
        1 * httpRequest.getRemoteAddr() >> '1.2.3.4'
        1 * loanService.create('johndoe', 123.45, 30, '1.2.3.4')
    }

    def 'when invalid amount should throw exception'() {
        when:
        loanController.create('johndoe', -1.00, 30, httpRequest)

        then:
        1 * amountValidator.validate(-1.00) >> { throw new IllegalArgumentException() }
        thrown(IllegalArgumentException)
    }

    def 'when invalid term should throw exception'() {
        when:
        loanController.create('johndoe', 123.45, -1, httpRequest)

        then:
        1 * termValidator.validate(-1) >> { throw new IllegalArgumentException() }
        thrown(IllegalArgumentException)
    }

    def 'should extend loan'() {
        when:
        loanController.extendTerm('johndoe', 12345, 60)

        then:
        1 * loanService.extend('johndoe', 12345, 60)
    }

    def 'when invalid extension term should throw exception'() {
        when:
        loanController.extendTerm('johndoe', 12345, -1)

        then:
        1 * termValidator.validate(-1) >> { throw new IllegalArgumentException() }
        thrown(IllegalArgumentException)
    }

    def 'should list all user loans'() {
        when:
        loanController.getAll('johndoe')

        then:
        1 * loanService.getAll('johndoe')
    }
}
