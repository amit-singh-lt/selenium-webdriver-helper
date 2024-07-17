package utility;

import org.apache.logging.log4j.*;
import org.testng.asserts.SoftAssert;

public class CustomSoftAssert extends SoftAssert {
    private final Logger ltLogger = LogManager.getLogger(CustomSoftAssert.class);

}
