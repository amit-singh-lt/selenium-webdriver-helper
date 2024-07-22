package pages;

import helper.LTHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.EnvSetup;

public class SignInSignUpPage extends LTHelper {
  private final Logger ltLogger = LogManager.getLogger(SignInSignUpPage.class);

  public void setUserDetails()  {
    EnvSetup.config.put("base64", generateBase64EncodedAuthToken(EnvSetup.USER_NAME, EnvSetup.USER_ACCESS_KEY));
    ltLogger.info("envConfig :- {}", EnvSetup.config);
  }
}
