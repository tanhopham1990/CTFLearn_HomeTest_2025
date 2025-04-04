package com.ctflearn.pageobjects.web;

import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.constants.ToastMessageConstant;
import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.utils.UserLogin;

public class LoginPage extends AbstractWebPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class.getSimpleName());

    private final String userNameOrEmailTextBox = "//input[@id='identifier']";
    private final String passwordTextBox = "//input[@id='password']";
    private final String loginButton = "//button[@type='submit']";

    public LoginPage(WebTestWrapper webTestWrapper) {
        super(webTestWrapper);
    }

    public void enterUsernameOrEmail(String usernameOrEmail) throws AutomationException {
        logger.info("Enter username or email {}", usernameOrEmail);
        webTestWrapper.inputText(userNameOrEmailTextBox, usernameOrEmail);
    }

    public void enterPassword(String password) throws AutomationException {
        logger.info("Enter password {}", password);
        webTestWrapper.inputText(passwordTextBox, password);
    }

    public DashboardPage clickOnLoginButton() throws AutomationException {
        logger.info("Click on Login button");
        webTestWrapper.clickControl(loginButton);
        return new DashboardPage(webTestWrapper);
    }

    public DashboardPage loginToDashboard(String username, String password) throws AutomationException {
        clickOnLoginItem();
        enterUsernameOrEmail(username);
        enterPassword(password);
        clickOnLoginButton();
        verifyToastMessage(ToastMessageConstant.LOGIN_SUCCESS.getName());
        return new DashboardPage(webTestWrapper);
    }

    public DashboardPage loginToDashboard(int userNumber) throws AutomationException {
        UserLogin userLogin = new UserLogin(userNumber);
        loginToDashboard(userLogin.getUsername(), userLogin.getPassword());
        return new DashboardPage(webTestWrapper);
    }

}
