package io.fourfinanceit.domain

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class LoanSpec extends Specification {

    def 'should be correct equals and hashcode'() {
        expect:
        EqualsVerifier.forClass(Loan).verify()
    }
}
