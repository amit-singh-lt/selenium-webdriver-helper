@androidDevicesRegression
Feature: Automation product regression for android devices

  @noBrowser @and1
  Scenario Outline: User is able to run test session with network true and tunnel true1
    Given Setup user details
    Then Initialize soft assert
    Then User start tunnel
#    And User create selenium driver with capabilities as <capabilities>
#    Then start session to test local
#    Then start session to test consoleLog
#    Then start session to test networkLog
#    Then start session to test badSsl
#    Then start session to test basicAuth
#    Then User stops tunnel
#    And User quits selenium driver
#    Then User verifies all artifacts via API

    Examples:
      | capabilities                                                                                              |
      | platformName=android,deviceName=Pixel 5,platformVersion=14,w3c=true,network=true,console=true,tunnel=true |


  @noBrowser @and2
  Scenario Outline: User is able to run test session with network true and tunnel true2
    Given Setup user details
    Then Initialize soft assert
#    Then User start tunnel
#    And User create selenium driver with capabilities as <capabilities>
#    Then start session to test local
#    Then start session to test consoleLog
#    Then start session to test networkLog
#    Then start session to test badSsl
#    Then start session to test basicAuth
#    Then User stops tunnel
#    And User quits selenium driver
#    Then User verifies all artifacts via API

    Examples:
      | capabilities                                                                                              |
      | platformName=android,deviceName=Pixel 5,platformVersion=14,w3c=true,network=true,console=true,tunnel=true |
