package io.fourfinanceit.service

import io.fourfinanceit.domain.Loan
import io.fourfinanceit.repository.LoanRepository
import spock.lang.Specification

import static io.fourfinanceit.domain.Loan.LoanStatus.APPROVED
import static io.fourfinanceit.domain.Loan.LoanStatus.RISK_ANALYSIS

class LoanServiceSpec extends Specification {

    def loanRepository = Mock LoanRepository
    def applicationCountChecker = Mock ApplicationCountChecker
    def nightPeriodChecker = Mock NightPeriodChecker
    def INTEREST_RATE = 10.0
    def EXTENSION_COEFFICIENT = 1.5

    def loanService = new LoanService(loanRepository, applicationCountChecker, nightPeriodChecker, INTEREST_RATE, EXTENSION_COEFFICIENT)

    def 'should create loan'() {
        when:
        def loan = loanService.create('johndoe', 123.45, 30, '1.2.3.4')

        then:
        1 * applicationCountChecker.validate('1.2.3.4') >> true
        1 * nightPeriodChecker.validate(123.45) >> true
        1 * loanRepository.save(_)
        loan.username == 'johndoe'
        loan.amount == 123.45
        loan.term == 30
        loan.interestRate == INTEREST_RATE
        loan.status == APPROVED
        loan.createdAt
        !loan.extensionTerm
        !loan.extensionCoefficient
        !loan.extendedAt
    }

    def 'when rate limit exceeded should perform risk analysis'() {
        when:
        def loan = loanService.create('johndoe', 123.45, 30, '1.2.3.4')

        then:
        1 * applicationCountChecker.validate('1.2.3.4') >> false
        loan.status == RISK_ANALYSIS
    }

    def 'when night period check fails should perform risk analysis'() {
        when:
        def loan = loanService.create('johndoe', 123.45, 30, '1.2.3.4')

        then:
        1 * applicationCountChecker.validate('1.2.3.4') >> true
        1 * nightPeriodChecker.validate(123.45) >> false
        loan.status == RISK_ANALYSIS
    }

    def 'should extend term'() {
        when:
        def loan = loanService.extend('johndoe', 12345, 60)

        then:
        1 * loanRepository.findOneByIdAndUsername(12345, 'johndoe') >> new Loan(id: 12345, username: 'johndoe')
        1 * loanRepository.save(_)
        loan.id == 12345
        loan.extensionTerm == 60
        loan.extensionCoefficient == EXTENSION_COEFFICIENT
        loan.extendedAt
    }

    def 'when loan to be extended not found should throw exception'() {
        when:
        loanService.extend('johndoe', 12345, 120)

        then:
        1 * loanRepository.findOneByIdAndUsername(12345, 'johndoe') >> null
        thrown(IllegalArgumentException)
    }

    def 'when loan to be extended already extended should throw exception'() {
        when:
        loanService.extend('johndoe', 12345, 120)

        then:
        1 * loanRepository.findOneByIdAndUsername(12345, 'johndoe') >> new Loan(extendedAt: new Date())
        thrown(IllegalArgumentException)
    }

    def 'should list all user loans'() {
        when:
        loanService.getAll('johndoe')

        then:
        1 * loanRepository.findByUsernameOrderByCreatedAtAsc('johndoe') >> []
    }
}
