@sauceLabs
Feature: Automation product regression for android devices

  Scenario: User is able to run test session with network true and tunnel true
    Given Setup user details
    Then Initialize soft assert
    And User create selenium driver
    Then start test action for consoleLog
    Then start test action for networkLog
    Then start test action for badSsl
    Then start test action for basicAuth
    And User quits selenium driver
