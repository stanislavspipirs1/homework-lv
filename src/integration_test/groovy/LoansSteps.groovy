import cucumber.api.groovy.EN
import cucumber.api.groovy.Hooks
import groovyx.net.http.HTTPBuilder
import io.fourfinanceit.HomeworkApplication
import static org.springframework.boot.SpringApplication.exit
import static org.springframework.boot.SpringApplication.run

this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

def context

Before {
    if (!context) {
        context = run HomeworkApplication
        context.addShutdownHook {
            exit context
        }
    }

    def file = new File(getClass().getResource('application.properties').getFile())
    def properties = new Properties()
    file.withInputStream { properties.load(it) }

    EXPECTED_INTEREST_RATE = new BigDecimal(properties.'interest.rate'.toString())
    EXPECTED_EXTENSION_COEFFICIENT = new BigDecimal(properties.'interest.rate.extension.coefficient'.toString())
    def port = Long.parseLong(properties.'server.port'.toString())
    REST_CLIENT = new HTTPBuilder( "http://localhost:${port}")
}

When(~'(.*) creates loan with amount (.*) and term (.*) days') { String username, BigDecimal amount, long term ->
    loan = REST_CLIENT.post(path: '/loans', query: ['username': username, 'amount': amount, 'term': term])
    assert loan.id
    assert loan.username == username
    assert loan.amount == amount
    assert loan.term == term
    assert loan.interestRate == EXPECTED_INTEREST_RATE
    assert loan.status == 'APPROVED' || loan.status == 'RISK_ANALYSIS'
    assert loan.createdAt
    assert !loan.extendedTerm
    assert !loan.extensionCoefficient
    assert !loan.extendedAt
}

Then(~'(.*) can see loan with amount (.*) and term (.*) days in the list') { String username, BigDecimal amount, long term ->
    loans = REST_CLIENT.get(path: "/loans/${username}")
    assert loans.last().id == loan.id
    assert loans.last().username == username
    assert loans.last().amount == amount
    assert loans.last().term == term
}

And(~'(.*) extends loan with amount (.*) and term (.*) days for (.*) days') { String username, BigDecimal amount,
                                                                              long term, long extensionTerm ->
    extendedLoan = REST_CLIENT.post(path: "/loans/extend/${loan.id}", query: ['username': username, 'term': extensionTerm ])
    assert extendedLoan.id == loan.id
    assert extendedLoan.username == username
    assert extendedLoan.amount == amount
    assert extendedLoan.term == term
    assert extendedLoan.interestRate == EXPECTED_INTEREST_RATE
    assert extendedLoan.status == 'APPROVED' || loan.status == 'RISK_ANALYSIS'
    assert extendedLoan.createdAt
    assert extendedLoan.extensionTerm == extensionTerm
    assert extendedLoan.extensionCoefficient == EXPECTED_EXTENSION_COEFFICIENT
    assert extendedLoan.extendedAt
}

Then(~'(.*) can see loan with extended term (.*) days in the list') { String username, long extensionTerm ->
    loans = REST_CLIENT.get(path: "/loans/${username}")
    assert loans.last().id == extendedLoan.id
    assert loans.last().extensionTerm == extensionTerm
}
