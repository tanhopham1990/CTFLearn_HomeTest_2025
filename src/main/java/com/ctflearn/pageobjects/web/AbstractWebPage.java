package com.ctflearn.pageobjects.web;

import com.ctflearn.utils.CommonUtil;
import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.constants.ToastMessageConstant;
import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractWebPage {

    private static final Logger logger = LogManager.getLogger(AbstractWebPage.class.getSimpleName());

    protected static String systemUploadFileInput = "//input[@id='file-upload']";
    protected static String loginItem = "//a[@href='/user/login']";
    protected static String challengesDropdownItem = "//a[@id='navbarDropdownMenuLink']/following-sibling::a[contains(@class,'dropdown-toggle')]";
    protected static String createChallengeDropdownItem = "//a[@href='/challenge/create']";
    protected static String profileIcon = "//a[@id='profileDropdown']";
    protected static String myChallenges = "//div[@aria-labelledby='profileDropdown']/a[starts-with(@href, '/challenge/by/')]";
    protected static String logOutButton = "//div[@aria-labelledby='profileDropdown']/a[@href='/user/logout']";
    protected static String toastMessage = "//div[@id='toast-1']//strong";
    protected static String toastMessageCloseButton = "//div[@id='toast-1']//button";

    protected WebTestWrapper webTestWrapper;

    public AbstractWebPage(WebTestWrapper webTestWrapper) {
        this.webTestWrapper = webTestWrapper;
    }

    public void uploadFile(String filePath) throws AutomationException {
        logger.info("Upload file: [{}]", filePath);
        webTestWrapper.isElementPresent(systemUploadFileInput);
        webTestWrapper.uploadFile(systemUploadFileInput, filePath);
    }

    public void clickOnLoginItem() throws AutomationException {
        logger.info("Click on Login item");
        webTestWrapper.clickControl(loginItem);
    }

    public ChallengeListPage clickOnMyChallengeItem() throws AutomationException {
        logger.info("Click on My Challenges item");
        webTestWrapper.clickControl(myChallenges);
        return new ChallengeListPage(webTestWrapper);
    }

    public LoginPage clickOnLogoutItem() throws AutomationException {
        logger.info("Click on Logout button");
        webTestWrapper.clickControl(logOutButton);
        verifyToastMessage(ToastMessageConstant.LOGOUT_SUCCESS.getName());
        return new LoginPage(webTestWrapper);
    }

    public void navigateToProfileIcon() throws AutomationException {
        logger.info("Click on profile icon");
        webTestWrapper.clickControl(profileIcon);
    }

    public void navigateToChallengesDropdownItem() throws AutomationException {
        logger.info("Click on Challenges dropdown item");
        webTestWrapper.clickControl(challengesDropdownItem);
    }

    public CreateChallengePage selectCreateChallengeDropdownItem() throws AutomationException {
        logger.info("Click on Create Challenge dropdown item");
        webTestWrapper.clickControl(createChallengeDropdownItem);
        return new CreateChallengePage(webTestWrapper);
    }

    public void verifyToastMessage(String message) throws AutomationException {
        logger.info("Validate toast message: {}", message);
        webTestWrapper.isElementPresent(toastMessage);
        validateTextEquals(webTestWrapper.getText(toastMessage), message);
        CommonUtil.sleepSeconds(1);
        webTestWrapper.clickControl(toastMessageCloseButton);
    }

    public void validateTextEquals(String actualText, String expectedText) throws AutomationException {
        logger.info("Validate text equals [{}]", expectedText);
        if (!actualText.equals(expectedText)) {
            throw new AutomationException("Actual text: [%s] / Expected text: [%s]", actualText, expectedText);
        }
    }

}

