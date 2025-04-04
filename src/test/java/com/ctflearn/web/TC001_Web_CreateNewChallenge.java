package com.ctflearn.web;

import com.aventstack.extentreports.Status;
import com.ctflearn.exception.AutomationException;
import com.ctflearn.pageobjects.web.*;
import com.ctflearn.utils.ExtentManager;
import org.testng.annotations.Test;
import com.ctflearn.dataobjects.NewChallengeInfo;

public class TC001_Web_CreateNewChallenge extends WebTestCaseBase {

    @Test(groups = {"webCases"})
    public void verifyNewChallengeCreatedSuccessfully() throws AutomationException {
        ExtentManager.createTest("Verify new challenge created successfully");

        int userNumber = 1;
        LoginPage loginPage = openCtfLearnApp();
        DashboardPage dashboardPage = loginPage.loginToDashboard(userNumber);

        dashboardPage.navigateToChallengesDropdownItem();
        NewChallengeInfo newChallengeInfo = new NewChallengeInfo();
        CreateChallengePage createChallengePage = dashboardPage.selectCreateChallengeDropdownItem();

        createChallengePage.createNewChallenge(newChallengeInfo);

        dashboardPage.navigateToProfileIcon();
        int firstCard = 1;
        ChallengeListPage challengeListPage = dashboardPage.clickOnMyChallengeItem();
        challengeListPage.validateChallengeCardInfoAtIndex(newChallengeInfo, firstCard);
        ChallengeDetailPage challengeDetailPage = challengeListPage.clickOnCardAtIndex(firstCard);
        challengeDetailPage.validateAllInfoInChallengeCard(newChallengeInfo);

        dashboardPage.navigateToProfileIcon();
        dashboardPage.clickOnLogoutItem();
    }
}
