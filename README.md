# selenium-webdriver-helper

CUCUMBER_FILTER_TAGS="@androidDevicesRegression" mvn test -DENV=prod -Dcucumber.features=src/test/resources/features/OS/AndroidDevicesRegression.feature -DsuiteXmlFile=testng.xml -DPARALLEL=10 -Drp.rerun=true