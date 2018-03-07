Feature: Loans
  In order to receive loan
  User must be able to create, extend and see own loans

  Scenario: Loan creation
    When John creates loan with amount 123.45 and term 30 days
    Then John can see loan with amount 123.45 and term 30 days in the list

  Scenario: Loan extension
    When Jane creates loan with amount 543.21 and term 45 days
    And Jane extends loan with amount 543.21 and term 45 days for 60 days
    Then Jane can see loan with extended term 60 days in the list