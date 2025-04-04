package com.ctflearn.mobile;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.ExtentManager;
import org.testng.annotations.Test;
import com.ctflearn.pageobjects.mobile.CopyFxMobilePage;
import com.ctflearn.pageobjects.mobile.LoginMobilePage;

public class TC001_Mobile_ViewPortfolio extends MobileTestCaseBase {

    @Test
    public void viewPortfolioSuccessfully() throws AutomationException {
        ExtentManager.createTest("View portfolio successfully");

        int userNumber = 2;
        LoginMobilePage loginMobilePage = dashboardMobilePage.tapOnSignInButton();
        dashboardMobilePage = loginMobilePage.loginToDashboard(userNumber);
        CopyFxMobilePage copyFxMobilePage = dashboardMobilePage.tapOnCopyFxButton();
        copyFxMobilePage.verifyPortfolioDisplays();
        dashboardMobilePage.tapOnToggleMenuButton();
        dashboardMobilePage.tapOnLogoutButton();
    }
}
