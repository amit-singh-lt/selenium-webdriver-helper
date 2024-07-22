package stepdefinitions;

import io.cucumber.java.en.And;
import utility.TestActions;

public class TestActionsStepDefinition extends TestActions {
    @And("^start test action for ([a-zA-Z0-9_=,: ]+)$")
    public void runSessionForTestingOnEmulatorSimulator(String testAction) {
        String maxReRun = System.getProperty("MAX_RERUN_TEST_ACTIONS", "1");
        for(int i = 0; i < Integer.parseInt(maxReRun); i++) {
            runTestActions(testAction);
        }
    }
}
