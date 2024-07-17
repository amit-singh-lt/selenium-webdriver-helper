package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestActions extends AutomationHelper {
    private final Logger ltLogger = LogManager.getLogger(TestActions.class);
    Base base = new Base();

    public void runTestActions(String testAction) {
        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
        boolean isTestActionPassed = false;
        String testActionFailedMessage = null;
        int retries = 1;
        switch (testAction) {
            case "local":
                isTestActionPassed = base.retry(retries, this::localTunnelTestAction);
                testActionFailedMessage = "Local Tunnel";
                break;
            case "consoleLog":
                isTestActionPassed = base.retry(retries, this::consoleLogTestAction);
                testActionFailedMessage = "Console Log";
                break;
            case "networkLog":
                isTestActionPassed = base.retry(retries, this::networkLogTestAction);
                testActionFailedMessage = "Network Log";
                break;
            case "badSsl":
                isTestActionPassed = base.retry(retries, this::badSslTestAction);
                testActionFailedMessage = "Bad SSL";
                break;
            case "basicAuth":
                isTestActionPassed = base.retry(retries, this::basicAuthTestAction);
                testActionFailedMessage = "Basic Auth";
                break;
            default:
                break;
        }
        if (!isTestActionPassed) {
            ltLogger.error("{} test action verification failed after {} retries", testActionFailedMessage, retries);
            softAssert.fail(String.format("%s test action verification failed after %s retries", testActionFailedMessage, retries));
        }
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }
}
