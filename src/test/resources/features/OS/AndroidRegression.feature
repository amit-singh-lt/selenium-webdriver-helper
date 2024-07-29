# Sumo Logic Query :-

# (_index=prod_test_datails)
# | where user_status != "Internal" and user_status != "Junk"
# | where test_env_os contains "Android"
# | where product IN ("Mobile Browser Automation SE")
# | count by product, test_env_browser, test_env_os, test_env_device, test_selenium_version, appium_version
# | sort _count desc

# https://docs.google.com/spreadsheets/d/1qjZjBBrmo6XML9csGPzLhlOTNK20OSxl_BHOxlAzQQg/edit?gid=0#gid=0

@androidRegression
Feature: Regression test cases for Android OS

  @noBrowser
  Scenario Outline: User is able to run test session with network true/false and tunnel true
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
    Then User verifies video artifacts via API
    Then User verifies commandLogs artifacts via API

    @androidRegression1
    Examples:
      | capabilities                                                                                              |
      | platformName=android;deviceName=Pixel 5;platformVersion=13;w3c=true;network=true;console=true;visual=true |
