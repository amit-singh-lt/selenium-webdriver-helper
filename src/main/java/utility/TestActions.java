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
        int maxRetriesOnFailure = 2;
        switch (testAction) {
            case "local":
                isTestActionPassed = base.retry(maxRetriesOnFailure, this::localTunnelTestAction);
                testActionFailedMessage = "Local Tunnel";
                break;
            case "consoleLog":
                isTestActionPassed = base.retry(maxRetriesOnFailure, this::consoleLogTestAction);
                testActionFailedMessage = "Console Log";
                break;
            case "networkLog":
                isTestActionPassed = base.retry(maxRetriesOnFailure, this::networkLogTestAction);
                testActionFailedMessage = "Network Log";
                break;
            case "badSsl":
                isTestActionPassed = base.retry(maxRetriesOnFailure, this::badSslTestAction);
                testActionFailedMessage = "Bad SSL";
                break;
            case "basicAuth":
                isTestActionPassed = base.retry(maxRetriesOnFailure, this::basicAuthTestAction);
                testActionFailedMessage = "Basic Auth";
                break;
            default:
                break;
        }
        if (!isTestActionPassed) {
            ltLogger.error("{} test action verification failed after {} retries", testActionFailedMessage, maxRetriesOnFailure);
            softAssert.fail(String.format("%s test action verification failed after %s retries", testActionFailedMessage, maxRetriesOnFailure));
        }
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }
}
