package utility;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
        Map<String, Object> customCapsMaps = new HashMap<>();

        for (String pair : capabilitiesArrayPair) {
            String[] keyValue = pair.split("=");
            if (keyValue.length < 2) {
                ltLogger.warn("Either key or Value is missing and the length is !2, hence skipping this iteration");
                continue;
            }

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            if (key.equalsIgnoreCase("tunnel") && value.equalsIgnoreCase("true")) {
                ltOptions.put("tunnel", true); // tunnel
                ltOptions.put("tunnelName", EnvSetup.TUNNEL_NAME_THREAD_LOCAL.get());
            } else if (key.equalsIgnoreCase(chromeOptions)) {
//                ltOptions.put(chromeOptions, addChromeOptions(value));
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                customCapsMaps.put("goog:chromeOptions", chromeOptions.getCapability("goog:chromeOptions"));
                ltOptions.putAll(customCapsMaps);
            } else if (TRUE_STRING.equalsIgnoreCase(value) || FALSE_STRING.equalsIgnoreCase(value)) {
                ltOptions.put(key, Boolean.parseBoolean(value)); // boolean capabilities
            } else {
                ltOptions.put(key, value); // others
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
            capabilities = capabilities + ";" + cliCaps;
        } else {
            if (capabilities == null || capabilities.isEmpty()) {
                ltLogger.warn("userCapability received in parameter is null, and returning empty lt:options response.");
                return Collections.emptyMap();
            }
        }
        caps = getHashMapFromString(capabilities.split(";"));
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


    public ChromeOptions addChromeOptions(String options) {
        ChromeOptions chromeOptions = new ChromeOptions();

        String[] argumentsArray = options.split(",");
        for (String argument : argumentsArray) {
            ltLogger.info("Chrome Options Pair is :- {}", argument);

            if (argument.contains("=")) {
                String[] keyValue = argument.split("=");
                if (keyValue.length < 2) {
                    ltLogger.warn("Either key or Value in CHROME_OPTIONS is missing and the length is !2, hence skipping this iteration");
                    continue;
                }

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                ltLogger.info("For Chrome Options Pair :- {}, Key is :- {}, and Value is :- {}", argument, key, value);

                chromeOptions.addArguments("--" + key + "=" + value);
            } else {
                chromeOptions.addArguments("--" + argument);
            }
        }
        ltLogger.info("goog:chromeOptions passed in Capabilities are :- {}", chromeOptions);
        return chromeOptions;
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
            EnvSetup.SELENIUM_TEST_DRIVER_SESSION_ID_THREAD_LOCAL.set(sessionId);
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


    public MutableCapabilities capabilitiesEmulatorWebDriver() {
        MutableCapabilities caps = new MutableCapabilities();
        MutableCapabilities sauceOptions = new MutableCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("appium:deviceName", "Google Pixel 4 GoogleAPI Emulator");
        caps.setCapability("appium:platformVersion", "14.0");
        caps.setCapability("appium:automationName", "UiAutomator2");
        sauceOptions.setCapability("appiumVersion", "2.11.0");
        sauceOptions.setCapability("username", EnvSetup.USER_NAME);
        sauceOptions.setCapability("accessKey", EnvSetup.USER_ACCESS_KEY);
        sauceOptions.setCapability("build", "SauceLab Build");
        sauceOptions.setCapability("name", "SauceLab Test");
        sauceOptions.setCapability("deviceOrientation", "PORTRAIT");
        caps.setCapability("sauce:options", sauceOptions);
        return caps;
    }

    public RemoteWebDriver driverCreateSauceLab() throws MalformedURLException, URISyntaxException {
        MutableCapabilities caps = capabilitiesEmulatorWebDriver();
        URI uri = new URI(EnvSetup.GRID_URL);
        ltLogger.info("URI :- {}", uri);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        RemoteWebDriver testDriver = new RemoteWebDriver(uri.toURL(), caps);
        stopWatch.stop();
        EnvSetup.SELENIUM_TEST_DRIVER_THREAD_LOCAL.set(testDriver);
        EnvSetup.SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL.set((int) stopWatch.getTime(TimeUnit.SECONDS));
        ltLogger.info("Driver Creation Time :- {} seconds", EnvSetup.SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL.get());
        String sessionId = testDriver.getSessionId().toString();
        ltLogger.info("Session ID :- {}", sessionId);
        return testDriver;
    }
}
