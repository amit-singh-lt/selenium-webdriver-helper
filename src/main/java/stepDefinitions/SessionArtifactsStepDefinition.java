package stepdefinitions;

import artifacts.SessionDetails;
import artifacts.Artifacts;
import helper.APIHelper;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Constant;
import utility.EnvSetup;

import java.util.HashMap;
import java.util.Map;

public class SessionArtifactsStepDefinition {
    private final Logger ltLogger = LogManager.getLogger(SessionArtifactsStepDefinition.class);
    String sessionId = null;
    APIHelper api = new APIHelper();
    Response sessionDetailsResponse;

    // https://api.lambdatest.com/api/v1/test/<sessionId>
    @Then("User gets Test Details using Session ID")
    public void verifyAllArtifactsViaAPI() {
        SessionDetails sessionDetails = new SessionDetails();
        if(sessionId == null) {
            sessionId = EnvSetup.SELENIUM_TEST_DRIVER_SESSION_ID_THREAD_LOCAL.get();
        }
        String sessionDetailsUri = sessionDetails.getSessionDetailUri(sessionId);
        Map<String, Object> headers = new HashMap<>();
        headers.put(Constant.AUTHORIZATION, "Basic " + EnvSetup.config.get("base64"));
        sessionDetailsResponse = api.httpMethod(Constant.GET, sessionDetailsUri, null, null, headers, null, 200);
        String sessionDetailsResponsePreetyPrint = sessionDetailsResponse.asString();
        ltLogger.info("Response is :- {}", sessionDetailsResponsePreetyPrint);
    }

    @Then("^User verifies (all|video|commandLogs) artifacts via API$")
    public void verifyVideoViaAPI(String what) {
        Artifacts artifacts = new Artifacts();
        switch (what) {
            case "video":
                artifacts.checkVideo();
                break;
            case "commandLogs":
                artifacts.checkCommandLogs();
                break;
            default:
                break;
        }
    }
}
