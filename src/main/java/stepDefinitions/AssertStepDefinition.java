package stepdefinitions;

import io.cucumber.java.en.Then;
import utility.CustomSoftAssert;
import utility.EnvSetup;

public class AssertStepDefinition {
    @Then("^Initialize soft assert$")
    public void initializeSoftAssert() {
        CustomSoftAssert softAssert = new CustomSoftAssert();
        EnvSetup.SOFT_ASSERT.set(softAssert);
    }
}
