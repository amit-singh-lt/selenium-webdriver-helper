package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.yaml.snakeyaml.Yaml;

public class EnvSetup {
  private static final Logger ltLogger = LogManager.getLogger(EnvSetup.class);

  public static final String ENV = System.getProperty("ENV");
  public static Map<String, String> config = getEnvConfig();
  public static final String USER_EMAIL = config.get("userEmail");
  public static final String USER_PASS = config.get("userPass");
  public static final String USER_NAME = config.get("userName");
  public static final String USER_ACCESS_KEY = config.get("userAccessKey");
  public static final String GRID_URL = config.get("seleniumGridURL");
  public static final String HOST_API_URL = config.get("hostApiUrl");
  public static final ThreadLocal<CustomSoftAssert> SOFT_ASSERT = new ThreadLocal<>();
  public static final ThreadLocal<String> TUNNEL_NAME_THREAD_LOCAL = new ThreadLocal<>();
  public static final ThreadLocal<Map<String, Object>> LT_OPTIONS_THREAD_LOCAL = new ThreadLocal<>();
  public static final ThreadLocal<DesiredCapabilities> CAPABILITIES_THREAD_LOCAL = ThreadLocal.withInitial(DesiredCapabilities::new);
  public static final ThreadLocal<RemoteWebDriver> SELENIUM_TEST_DRIVER_THREAD_LOCAL = new ThreadLocal<>();
  public static final ThreadLocal<String> SELENIUM_TEST_DRIVER_SESSION_ID_THREAD_LOCAL = new ThreadLocal<>();
  public static final ThreadLocal<RemoteWebDriver> SELENIUM_CLIENT_DRIVER_THREAD_LOCAL = new ThreadLocal<>();
  public static final ThreadLocal<Integer> SELENIUM_DRIVER_CREATION_TIME_THREAD_LOCAL = new ThreadLocal<>();

  public static Map<String, String> getEnvConfig() {
    File yamlFile = new File("src/test/resources/cucumber.yml");
    Yaml ymlFileReader = new Yaml();
    try (InputStream inStr = new FileInputStream(yamlFile)) {
      Map<String, Object> ymlObj = ymlFileReader.load(inStr);
      Object envValue = ymlObj.get(ENV);
      if (envValue instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, String> envConfig = (Map<String, String>) envValue;
        return envConfig;
      } else {
        ltLogger.error("ERROR: Expected a Map<String, String> but got {}", envValue.getClass().getName());
        return Collections.emptyMap();
      }
    } catch (FileNotFoundException e) {
      ltLogger.error("ERROR: cucumber.yml file is not found.", e);
      return Collections.emptyMap();
    } catch (ClassCastException e) {
      ltLogger.error("ERROR: The loaded value cannot be cast to Map<String, String>.", e);
      return Collections.emptyMap();
    } catch (Exception e) {
      ltLogger.error("ERROR: Unexpected error occurred while loading the configuration.", e);
      return Collections.emptyMap();
    }
  }
}
