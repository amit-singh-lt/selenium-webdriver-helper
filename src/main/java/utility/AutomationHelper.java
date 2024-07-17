package utility;

import helper.WebDriverHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.util.Map;

import static utility.Locators.*;

public class AutomationHelper extends Base {
    private final Logger ltLogger = LogManager.getLogger(AutomationHelper.class);

    WebDriverHelper wdHelper;

    public AutomationHelper() {
        wdHelper = new WebDriverHelper();
    }

    public void localTunnelTestAction() {
        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
        wdHelper.getURL(localHostUrl);
        softAssert.assertEquals(wdHelper.getText(LOCAL_SITE_HEADING), "Directory listing for /", "Local Site Heading does not match");
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }

    public void addConsoleLogToSessionResults(String consoleLog) {
        Map<String, Object> ltOptions = EnvSetup.LT_OPTIONS_THREAD_LOCAL.get();
        if (ltOptions.get(consoleLogs) == null || ltOptions.get(consoleLogs).toString().equalsIgnoreCase("")) {
            ltOptions.put(consoleLogs, consoleLog);
        } else {
            ltOptions.put(consoleLogs, ltOptions.get(consoleLogs) + ">" + consoleLog);
        }
        ltLogger.info("ltOptions.get(\"consoleLogs\") :- {}", ltOptions.get(consoleLogs));
        EnvSetup.LT_OPTIONS_THREAD_LOCAL.set(ltOptions);
    }

    public void consoleLogTestAction() {
        wdHelper.getURL(ltStageLambdaDevopsUrl);

        wdHelper.javascriptExecution("console.error('console log is working fine via \"ERROR command\"')");
        addConsoleLogToSessionResults(consoleMsg + " via error command in browser");
        ltLogger.info("console.error('console log is working fine via \"ERROR command\"')");
        
        wdHelper.javascriptExecution("console.log('console log is working fine via \"LOG command\"')");
        addConsoleLogToSessionResults(consoleMsg + " via log command in browser");
        ltLogger.info("console.log('console log is working fine via \"LOG command\"')");
        
        wdHelper.javascriptExecution("console.warn('console log is working fine via \"WARN command\"')");
        addConsoleLogToSessionResults(consoleMsg + " via warn command in browser");
        ltLogger.info("console.warn('console log is working fine via \"WARN command\"')");
        
        wdHelper.javascriptExecution("console.info('console log is working fine via \"INFO command\"')");
        addConsoleLogToSessionResults(consoleMsg + " via info command in browser");
        ltLogger.info("console.info('console log is working fine via \"INFO command\"')");
    }

    public void networkLogTestAction() {
        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
        for (int i = 0; i < networkUrls.length; i++) {
            wdHelper.getURL(networkUrls[i]);
            String actualTitle = wdHelper.getTitle();
            softAssert.assertEquals(actualTitle, networkUrlsTitle[i], String.format("URL Title did not matched, Expected :- %s, Actual :- %s", networkUrlsTitle[i], actualTitle));
        }
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }

    public void badSslTestAction() {
        // https://badssl.com/
        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
        for (int i = 0; i < badSslUrls.length; i++) {
            wdHelper.getURL(badSslUrls[i]);
            String actualBadSslH1 = wdHelper.getText(BAD_SSL_H1);
            softAssert.assertEquals(actualBadSslH1, badSslUrlsTitle[i], String.format("URL page is not displayed. Expected :- %s, Actual :- %s", actualBadSslH1, badSslUrlsTitle[i]));
        }
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }

    public void basicAuthTestAction() {
        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
        try {
            wdHelper.getURL(internetHerokuAppBasicAuthUrl);
            WebElement basicAuthElement = wdHelper.waitForElement(BASIC_AUTH_HEADING, BALANCED_WAIT_TIME);
            String basicAuthHeading = basicAuthElement.getText();
            softAssert.assertEquals(basicAuthHeading, internetHerokuAppBasicAuthUrlH3, "Basic Auth Access has failed for :- " + internetHerokuAppBasicAuthUrl);
        } catch (Exception e) {
            ltLogger.error("Basic Auth access has failed for :- {}", internetHerokuAppBasicAuthUrl);
            wdHelper.getURL(authenticationTestHttpAuthUrl);
            WebElement basicAuthElement = wdHelper.waitForElement(AUTHENTICATION_TEST_HEADING, BALANCED_WAIT_TIME);
            String basicAuthHeading = basicAuthElement.getText();
            softAssert.assertEquals(basicAuthHeading, authenticationTestHttpAuthUrlH1, "Basic Auth Access has failed for :- " + authenticationTestHttpAuthUrl);
        } finally {
            EnvSetup.SOFT_ASSERT.set(softAssert);
        }
    }
}
