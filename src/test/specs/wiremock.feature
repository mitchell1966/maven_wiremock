Feature: test

  Scenario: wiremock

    Given the loaded data is 00005000
    Given the endpoint is ready
    When the endpoint is called
      | To date                | 2016-07-01 |
      | From date              | 2016-06-04 |
      | Minimum                | 530.00     |
      | Sort code              | 010616     |
      | Account number         | 00005000   |
      | Date of Birth          | 1984-07-27 |
      | User Id                | user12345  |
      | Account Holder Consent | true       |
    Then stuff happens

  Scenario: Wiremock Consent

    Given consent is granted for the following
      | Sort code      | 010616     |
      | Account number | 01078916   |
      | Date of birth  | 1984-07-27 |
    Then stuff happens