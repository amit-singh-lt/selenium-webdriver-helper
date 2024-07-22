package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.remote.RemoteWebDriver;
import utility.CapabilitiesHelper;

import java.util.Map;

public class CapabilitiesStepDefinition extends CapabilitiesHelper {
    private RemoteWebDriver testDriver;

    @Then("^User create selenium driver with capabilities as ([a-zA-Z0-9_=,;:.+\\- ]+)$")
    public void featureFileCapabilities(String capabilities) throws Exception {
        Map<String, Object> mapCapabilities = appendDynamicCapability(capabilities);
        testDriver = driverCreate(mapCapabilities);
    }

    @Then("^User create selenium driver$")
    public void featureFileCapabilitiesForSauceLabs() throws Exception {
        testDriver = driverCreateSauceLab();
    }

    @And("User quits selenium driver")
    public void quitDriver() {
        quitDriver(testDriver);
    }
}
