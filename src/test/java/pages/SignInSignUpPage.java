package pages;

import helper.LTHelper;
import org.apache.logging.log4j.*;
import utility.EnvSetup;

public class SignInSignUpPage extends LTHelper {
  private final Logger ltLogger = LogManager.getLogger(SignInSignUpPage.class);

  public void setUserDetails()  {
    String email = EnvSetup.USER_EMAIL;
    ltLogger.info("User Email :- {}", email);
    
    String pass = EnvSetup.USER_PASS;
    ltLogger.info("User Password :- {}", pass);
  }
}
