package com.ctflearn.mobile;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.pageobjects.mobile.DashboardMobilePage;
import com.ctflearn.utils.ExtentManager;
import org.testng.annotations.Test;
import com.ctflearn.pageobjects.mobile.CopyFxMobilePage;
import com.ctflearn.pageobjects.mobile.LoginMobilePage;

public class TC001_Mobile_ViewPortfolio extends MobileTestCaseBase {

    @Test(groups = {"androidCases", "iosCases"})
    public void viewPortfolioSuccessfully() throws AutomationException {
        ExtentManager.createTest("View portfolio successfully");

        ExtentManager.log("Launch the mobile trading app");
        int userNumber = 2;
        DashboardMobilePage dashboardMobilePage = openTradingMobileApp();

        ExtentManager.log("Login with valid credentials");
        LoginMobilePage loginMobilePage = dashboardMobilePage.tapOnSignInButton();
        dashboardMobilePage = loginMobilePage.loginToDashboard(userNumber);

        ExtentManager.log("Navigate to Portfolio and validate the displayed investment data.");
        CopyFxMobilePage copyFxMobilePage = dashboardMobilePage.tapOnCopyFxButton();
        copyFxMobilePage.verifyPortfolioDisplays();
        dashboardMobilePage.tapOnToggleMenuButton();

        ExtentManager.log("Log out.");
        dashboardMobilePage.tapOnLogoutButton();
    }
}
