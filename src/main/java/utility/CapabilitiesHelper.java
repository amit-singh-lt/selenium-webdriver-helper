package utility;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CapabilitiesHelper extends WaitConstant {
    private final Logger ltLogger = LogManager.getLogger(Capabilities.class);

    /**
     * Converts a comma-separated list of key=value pairs into a HashMap.
     *
     * @param capabilitiesArrayPair the comma-separated list of key=value pairs
     * @return a Map containing the key-value pairs, or an empty map if the input is null or empty
     */
    public Map<String, Object> getHashMapFromString(String[] capabilitiesArrayPair) {
        Map<String, Object> ltOptions = new HashMap<>();

        for (String pair : capabilitiesArrayPair) {
            String[] keyValue = pair.split("=");
            if (keyValue.length < 2) {
                ltLogger.warn("Either key or Value is missing and the length is 2, hence skipping this iteration");
                continue;
            }

            String parameter = keyValue[0].trim();
            String value = keyValue[1].trim();
            if (parameter.equalsIgnoreCase("tunnel") && value.equalsIgnoreCase("true")) {
                ltOptions.put("tunnel", true); // tunnel
                ltOptions.put("tunnelName", EnvSetup.TUNNEL_NAME_THREAD_LOCAL.get());
            } else if (TRUE_STRING.equalsIgnoreCase(value) || FALSE_STRING.equalsIgnoreCase(value)) {
                ltOptions.put(parameter, Boolean.parseBoolean(value)); // boolean capabilities
            } else {
                ltOptions.put(parameter, value); // others
            }
        }

        return ltOptions;
    }


    /**
     * Appends dynamic capabilities from system properties to the provided capabilities string.
     *
     * @param capabilities the initial capabilities as a comma-separated list of key=value pairs
     * @return a Map containing the combined key-value pairs from the input capabilities and system properties
     */
    public Map<String, Object> appendDynamicCapability(String capabilities) {
        Map<String, Object> caps;
        String cliCaps = System.getProperty("CAPS", "");
        if (cliCaps.contains("=")) {
            ltLogger.info("Caps passed from CLI :- {}", cliCaps);
            capabilities = capabilities + "," + cliCaps;
        } else {
            if (capabilities == null || capabilities.isEmpty()) {
                ltLogger.warn("userCapability received in parameter is null, and returning empty lt:options response.");
                return Collections.emptyMap();
            }
        }
        caps = getHashMapFromString(capabilities.split(","));
        EnvSetup.LT_OPTIONS_THREAD_LOCAL.set(caps);
        return caps;
    }


    /**
     * Handles exceptions that occur during driver creation.
     * Logs the error, sets the driver creation time to -1, and rethrows the exception.
     *
     * @param e The exception that occurred during driver creation.
     * @throws Exception The rethrown exception with additional context information.
     */
    private void handleDriverCreationException(Exception e) throws Exception {
        EnvSetup.SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL.set(-1);
        ltLogger.error("[DRIVER CREATION ERROR] Driver was not created");
        ltLogger.error(e);
        throw new Exception("[DRIVER CREATION ERROR] Driver was not created" + NEW_LINE + "Exception :- " + e + NEW_LINE + "Capabilities :- " + EnvSetup.LT_OPTIONS_THREAD_LOCAL.get() + NEW_LINE);
    }


    /**
     * Creates a RemoteWebDriver instance with the specified capabilities.
     * Logs the capabilities, constructs the URI, measures driver creation time, and sets up implicit wait timeout.
     *
     * @param mapCapabilities A map of capabilities to be set for the driver.
     * @return A RemoteWebDriver instance or null if an exception occurs.
     * @throws Exception If driver creation fails, the exception is handled and rethrown.
     */
    public RemoteWebDriver driverCreate(Map<String, Object> mapCapabilities) throws Exception {
        ltLogger.info("lt:options :- {}", mapCapabilities);
        EnvSetup.CAPABILITIES_THREAD_LOCAL.get().setCapability("lt:options", mapCapabilities);
        ltLogger.info("Capabilities :- {}", EnvSetup.CAPABILITIES_THREAD_LOCAL.get());
        try {
            URI uri = new URI(HTTPS + EnvSetup.USER_NAME + ":" + EnvSetup.USER_ACCESS_KEY + EnvSetup.GRID_URL);
            ltLogger.info("URI :- {}", uri);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            RemoteWebDriver testDriver = new RemoteWebDriver(uri.toURL(), EnvSetup.CAPABILITIES_THREAD_LOCAL.get());
            stopWatch.stop();
            EnvSetup.SELENIUM_TEST_DRIVER_THREAD_LOCAL.set(testDriver);
            EnvSetup.SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL.set((int) stopWatch.getTime(TimeUnit.SECONDS));
            ltLogger.info("Driver Creation Time :- {} seconds", EnvSetup.SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL.get());
            String sessionId = testDriver.getSessionId().toString();
            ltLogger.info("Session ID :- {}", sessionId);
            testDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(SHORT_WAIT_TIME));
            testDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(LONG_WAIT_TIME));
            String platformName = EnvSetup.LT_OPTIONS_THREAD_LOCAL.get().get("platformName").toString();
            if(!(platformName.equalsIgnoreCase("android") || platformName.equalsIgnoreCase("ios"))) {
                testDriver.manage().window().maximize();
            }
            return testDriver;
        } catch (Exception e) {
            handleDriverCreationException(e);
        }
        return null;
    }


    /**
     * Quits the WebDriver session if it is not null.
     * Logs the appropriate message based on whether the driver was successfully closed or if it was null.
     *
     * @param driver The RemoteWebDriver instance to be closed.
     */
    public void quitDriver(RemoteWebDriver driver) {
        if (driver != null) {
            driver.quit();
            ltLogger.info("Driver closed");
        } else {
            ltLogger.error("Driver object received is null");
        }
    }
}
