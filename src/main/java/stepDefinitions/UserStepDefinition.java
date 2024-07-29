package stepdefinitions;

import io.cucumber.java.en.Given;
import pages.SignInSignUpPage;

public class UserStepDefinition {
    SignInSignUpPage signInSignUpObj = new SignInSignUpPage();

    @Given("^Setup user details$")
    public void userSetup() {
        signInSignUpObj.setUserDetails();
    }
}
