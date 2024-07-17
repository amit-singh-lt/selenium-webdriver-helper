package stepDefinitions;

import io.cucumber.java.en.Then;
import utility.CustomSoftAssert;
import utility.EnvSetup;

public class SessionArtifactsStepDefinition {
//    @Then("^User verify (video|test logs|appium logs|network logs|device logs) via API$")
//    public void verifyTestSessionMetadata(String metadata) {
//        CustomSoftAssert softAssert = EnvSetup.SOFT_ASSERT.get();
//        EnvSetup.LOGGER_THREAD_LOCAL.get().info("I verify \"{}\" via API", metadata);
//        TestMetaDataBase.setSessionID(EnvSetup.SESSION_ID_THREAD_LOCAL.get());
//        try {
//            switch (metadata) {
//                case "video" -> VerifyVideo.verifyTestVideo();
//                case "test logs" -> VerifyTestLog.verifyTestLogs();
//                case "appium logs" -> VerifyAppiumLog.verifyAppiumLogs();
//                case "network logs" -> VerifyNetworkLog.verifyNetworkLogs();
//                case "device logs" -> VerifyDeviceLog.verifyDeviceLogs();
//                case "visual screenshot" -> VerifyVisualScreenshot.verifyVisualScreenshot();
//            }
//        } catch (Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("Exception came while verifying [{}]", metadata.toUpperCase());
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error(e);
//            setHookErrorStatusMessage(metadata + " verification");
//            softAssert.assertTrue(false, String.format("Exception came while verifying [%s]", metadata.toUpperCase()) + Constants.NEW_LINE + String.format("Exception :- %s", e));
//        }
//        EnvSetup.SOFT_ASSERT.set(softAssert);
//    }
//
//    @Then("User verifies all artifacts via API")
//    public void verifyAllArtifactsViaAPI() {
//
//
//        try {
//            verifyTestSessionMetadata("test logs");
//        } catch(Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("test logs verification failed");
//            setHookErrorStatusMessage("test logs verification");
//        }
//
//        try {
//            verifyTestSessionMetadata("appium logs");
//        } catch(Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("appium logs failed");
//            setHookErrorStatusMessage("appium logs verification");
//        }
//
//        try {
//            verifyTestSessionMetadata("network logs");
//        } catch(Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("network logs failed");
//            setHookErrorStatusMessage("network logs verification");
//        }
//
//        try {
//            verifyTestSessionMetadata("device logs");
//        } catch(Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("device logs failed");
//            setHookErrorStatusMessage("device logs verification");
//        }
//
//        try {
//            verifyTestSessionMetadata("visual screenshot");
//        } catch(Exception e) {
//            EnvSetup.LOGGER_THREAD_LOCAL.get().error("visual screenshots failed");
//            setHookErrorStatusMessage("visual screenshot verification");
//        }
//    }
}
