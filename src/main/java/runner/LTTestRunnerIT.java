package runner;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import io.cucumber.core.cli.Main;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import utility.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.epam.reportportal.cucumber.AbstractReporter.ITEM_TREE;
import static java.lang.System.*;
import static java.util.Optional.ofNullable;

@CucumberOptions(
        features = { "src/test/resources/features" },
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "rerun:rerun/failed_scenarios.txt",
                "com.epam.reportportal.cucumber.ScenarioReporter"
        }
)
public class LTTestRunnerIT extends AbstractTestNGCucumberTests {
  private static final Logger ltLogger = LogManager.getLogger(LTTestRunnerIT.class);

  public static void main(String[] args) {
    List<String> cucumberOptions = new ArrayList<>(Arrays.asList(
      "src/test/resources/features",
      "-g", "stepdefinitions",
      "-g", "hooks",
      "-p", "pretty",
      "-p", "rerun:rerun/failed_scenarios.txt",
      "-p", "com.epam.reportportal.cucumber.ScenarioReporter"
    ));
    setProperty("cucumber.publish.quiet", "true");
    for (String arg : args) {
      if (arg.startsWith("-DCUCUMBER_FILTER_TAGS=")) {
        String tags = arg.substring("-DCUCUMBER_FILTER_TAGS=".length());
        cucumberOptions.add("-t");
        cucumberOptions.add(tags);
      } else if (arg.startsWith("-D")) {
        String[] keyValue = arg.substring(2).split("=", 2);
        if (keyValue.length == 2) {
          setProperty(keyValue[0], keyValue[1]);
        } else {
          ltLogger.error("Invalid system property format :- {}", arg);
        }
      } else {
        cucumberOptions.add(arg);
      }
    }

    ltLogger.info("Running Cucumber with options: ");
    for (String option : cucumberOptions) {
      ltLogger.info(option);
    }

    Main.main(cucumberOptions.toArray(new String[0]));
  }

  public LTTestRunnerIT() {
    super();
    setProperty("log4j.configurationFile", "src/main/resources/log4j2.yaml");
  }

  @Override
  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    if (getProperty("RUN_N_TIMES") != null) {
      int runNTimes = Integer.parseInt(getProperty("RUN_N_TIMES"));
      Object[][] originalScenarios = super.scenarios();
      int totalScenarios = originalScenarios.length;
      Object[][] newScenarios = new Object[totalScenarios * runNTimes][];

      int index = 0;
      for (Object[] originalScenario : originalScenarios) {
        Object cucumberPickle = originalScenario[0];
        Object featureWrapperImpl = originalScenario[1];
        for (int j = 0; j < runNTimes; j++) {
          newScenarios[index++] = new Object[] { cucumberPickle, featureWrapperImpl };
        }
      }
      return newScenarios;
    }
    return super.scenarios();
  }

  @BeforeSuite
  public void initializeReportPortalParameters() {
    try (InputStream input = LTTestRunnerIT.class.getClassLoader().getResourceAsStream("reportportal.properties")) {
      if (input == null) {
        ltLogger.error("Sorry, unable to find reportportal.properties");
        return;
      }
      Properties prop = new Properties();
      prop.load(input);
      prop.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
    } catch (IOException ex) {
      ltLogger.error(ex);
    }
  }

  @AfterSuite
  public void getReportPortalURL() {
    if (getProperty("rp.project") != null && getProperty("rp.endpoint") != null) {
      ListenerParameters parameters = ofNullable(Launch.currentLaunch()).map(Launch::getParameters).orElseThrow(() -> new IllegalStateException("Launch not found"));
      String launchUuid = ITEM_TREE.getLaunchId().blockingGet();
      String rpLaunchURL = parameters.getBaseUrl() + "/ui/#" + parameters.getProjectName() + "/launches/all/" + launchUuid;
      out.println(Constant.NEW_LINE + Constant.NEW_LINE + "Report Portal URL :- " + rpLaunchURL + Constant.NEW_LINE);
    }
  }
}
