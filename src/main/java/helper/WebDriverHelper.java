package helper;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.EnvSetup;

import java.time.Duration;

import static utility.WaitConstant.*;

public class WebDriverHelper extends BaseClass {
  private final Logger ltLogger = LogManager.getLogger(WebDriverHelper.class);

  private final WebDriver driver;

  private static final String CLASS = "class";
  private static final String ID = "id";
  private static final String CSS = "css";
  private static final String XPATH = "xpath";
  private static final String NAME = "name";
  private static final String TAG_NAME = "tagname";

  public WebDriverHelper() {
    this.driver = EnvSetup.SELENIUM_TEST_DRIVER_THREAD_LOCAL.get();
  }

  public WebDriverHelper(RemoteWebDriver testDriver) {
    this.driver = testDriver;
  }

  public By findElementBy(String[] locator) {
    ltLogger.info("wait for element via, using ['{}','{}'] ", locator[0], locator[1]);
    String using = locator[0].toLowerCase();
    String locatorValue = locator[1];
    return switch (using) {
      case ID -> By.id(locatorValue);
      case CLASS -> By.className(locatorValue);
      case NAME -> By.name(locatorValue);
      case XPATH -> By.xpath(locatorValue);
      case CSS -> By.cssSelector(locatorValue);
      case TAG_NAME -> By.tagName(locatorValue);
      default -> throw new IllegalArgumentException("Unsupported locator strategy: " + using);
    };
  }

  public WebElement waitForElement(String[] locator, int timeout) {
    try {
      ltLogger.info("wait for element via, using ['{}','{}'] ", locator[0], locator[1]);
      this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
      return wait.until(ExpectedConditions.presenceOfElementLocated(findElementBy(locator)));
    } finally {
      this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(SHORT_WAIT_TIME));
    }
  }

  public void getURL(String url) {
    ltLogger.info("Open URL : ['{}'] ", url);
    this.driver.get(url);
  }

  public String getTitle() {
    return this.driver.getTitle();
  }

  public String getCurrentURL() {
    return this.driver.getCurrentUrl();
  }

  public void pageRefresh() {
    this.driver.navigate().refresh();
  }

  public void pageForward() {
    this.driver.navigate().forward();
  }

  public void pageBack() {
    this.driver.navigate().back();
  }

  public WebElement getElement(String[] locator, int waitTime) {
    ltLogger.info("find element via,using ['{}','{}'] ", locator[0], locator[1]);
    return waitForElement(locator, waitTime);
  }

//  public WebElement getVisibleElement(String[] locator, int waitTime, int pollingTime) {
//    return waitForElementToBeVisible(locator, waitTime, pollingTime);
//  }

  public WebElement getElement(String[] locator) {
    return getElement(locator, 30);
  }

  public String getText(WebElement ele) {
    return ele.getText().trim();
  }

  public String getText(String[] locator) {
    WebElement ele = getElement(locator);
    return getText(ele);
  }

  // JAVASCRIPT EXECUTION

  public void javascriptExecution(String script) {
    ltLogger.info("Script to execute :- {}", script);
    try {
      ((JavascriptExecutor) this.driver).executeScript(script);
      ltLogger.info("INFO: script executed successfully: {}", script);
    } catch (Exception e) {
      if(script.contains("console.error")) return;
      ltLogger.error("ERROR: script executed failed: {}", script);
      ltLogger.error("Exception :- {}", e.toString());
      throw e;
    }
  }

  public void hardRefresh() {
    ((RemoteWebDriver) this.driver).executeScript("location.reload(true);");
  }
}
