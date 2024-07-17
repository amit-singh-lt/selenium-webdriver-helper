package stepDefinitions;

import io.cucumber.java.en.And;
import utility.TestActions;

public class TestActionsStepDefinition extends TestActions {
    @And("^start session to test ([a-zA-Z0-9_=,: ]+)$")
    public void runSessionForTestingOnEmulatorSimulator(String testAction) {
        runTestActions(testAction);
    }
}
