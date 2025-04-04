package com.ctflearn.pageobjects.mobile;

import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.wrappers.MobileTestWrapper;

public class AbstractMobilePage {

    private static final Logger logger = LogManager.getLogger(AbstractMobilePage.class.getSimpleName());

    protected MobileTestWrapper mobileTestWrapper;

    protected static String skipButton = "//*[@content-desc=\"OnBoarding_skip_ViewButton\"]";
    protected static String notNowButton = "//*[@content-desc=\"RecommendUpdateScreen_NotNow_ViewButton\"]";
    protected static String signInButton = "//*[@content-desc=\"BottomNav_Auth\"]";
    protected static String copyFxButton = "//*[@content-desc=\"BottomNav_CopyFx\"]";
    protected static String toggleMenuButton = "//*[@content-desc=\"ToggleMenuButton\"]";
    protected static String logOutButton = "//*[@content-desc=\"SideMenu_Logout_ViewButton\"]";

    public AbstractMobilePage(MobileTestWrapper mobileTestWrapper) {
        this.mobileTestWrapper = mobileTestWrapper;
    }

    public void tapOnSkipButton() throws AutomationException {
        logger.info("Click on Skip button");
        mobileTestWrapper.clickControl(skipButton);
    }

    public void tapOnNotNowButton() throws AutomationException {
        logger.info("Click on Not Now button");
        mobileTestWrapper.clickControl(notNowButton);
    }

    public LoginMobilePage tapOnSignInButton() throws AutomationException {
        logger.info("Click on Sign In button");
        mobileTestWrapper.clickControl(signInButton);
        return new LoginMobilePage(mobileTestWrapper);
    }

    public CopyFxMobilePage tapOnCopyFxButton() throws AutomationException {
        logger.info("Click on CopyFx button");
        mobileTestWrapper.clickControl(copyFxButton);
        return new CopyFxMobilePage(mobileTestWrapper);
    }

    public void tapOnToggleMenuButton() throws AutomationException {
        logger.info("Tap on toggle menu button");
        mobileTestWrapper.clickControl(toggleMenuButton);
    }

    public void tapOnLogoutButton() throws AutomationException {
        logger.info("Tap on Logout button");
        mobileTestWrapper.clickControl(logOutButton);
    }

}
