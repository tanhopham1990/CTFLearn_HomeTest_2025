package com.ctflearn.pageobjects.mobile;

import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.utils.UserLogin;
import com.ctflearn.wrappers.MobileTestWrapper;

public class LoginMobilePage extends AbstractMobilePage {

    private static final Logger logger = LogManager.getLogger(LoginMobilePage.class.getSimpleName());

    private static String emailInput = "//*[@content-desc=\"Credentials_Email_InputView\"]";
    private static String passwordInput = "//*[@content-desc=\"Credentials__Password_InputView\"]";
    private static String loginButton = "//*[@content-desc=\"AuthForm_Login_ViewButton\"]";

    public LoginMobilePage(MobileTestWrapper mobileTestWrapper) {
        super(mobileTestWrapper);
    }

    public void enterEmail(String email) throws AutomationException {
        logger.info("Enter email: {}", email);
        mobileTestWrapper.inputText(emailInput, email);
    }

    public void enterPassword(String password) throws AutomationException {
        logger.info("Enter password: {}", password);
        mobileTestWrapper.inputText(passwordInput, password);
    }

    public void tapOnLoginButton() throws AutomationException {
        logger.info("Click on Login button");
        mobileTestWrapper.clickControl(loginButton);
    }

    public DashboardMobilePage loginToDashboard(String email, String password) throws AutomationException {
        enterEmail(email);
        enterPassword(password);
        tapOnLoginButton();
        tapOnNotNowButton();
        return new DashboardMobilePage(mobileTestWrapper);
    }

    public DashboardMobilePage loginToDashboard(int userNumber) throws AutomationException {
        UserLogin userLogin = new UserLogin(userNumber);
        loginToDashboard(userLogin.getUsername(), userLogin.getPassword());
        return new DashboardMobilePage(mobileTestWrapper);
    }
}
