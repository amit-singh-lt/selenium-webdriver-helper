@androidDevicesRegression
Feature: Automation product regression for android devices

  @noBrowser
  Scenario Outline: User is able to run test session with network true and tunnel true
    Given Setup user details
    Then Initialize soft assert
    Then User start tunnel
    And User create selenium driver with capabilities as <capabilities>
    Then start test action for local
    Then start test action for consoleLog
    Then start test action for networkLog
    Then start test action for badSsl
    Then start test action for basicAuth
    Then User stops tunnel
    And User quits selenium driver
    Then User gets Test Details using Session ID
    Then User verifies all artifacts via API

    Examples:
      | capabilities                                                                                              |
      | platformName=android;deviceName=Pixel 5;platformVersion=13;w3c=true;network=true;console=true;visual=true |
