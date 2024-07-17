package listeners;

import java.util.List;

import org.apache.logging.log4j.*;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

public class SuiteAlterListener implements IAlterSuiteListener {
  private static final Logger ltLogger = LogManager.getLogger(SuiteAlterListener.class);
  @Override
  public void alter(List<XmlSuite> suites) {
    String parallelCount = System.getProperty("PARALLEL");
    if (parallelCount != null) {
      try {
        int count = Integer.parseInt(parallelCount);
        XmlSuite suite = suites.getFirst();
        suite.setDataProviderThreadCount(count);
        ltLogger.info("Updated Parallel Count in XmlSuite to :- {}", count);
      } catch (NumberFormatException e) {
        ltLogger.error("Invalid format for parallelCount :- {}", parallelCount);
      }
    }
  }
}
