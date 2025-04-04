package com.ctflearn.web;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.pageobjects.web.*;
import com.ctflearn.utils.ExtentManager;
import org.testng.annotations.Test;
import com.ctflearn.dataobjects.NewChallengeInfo;

public class TC001_Web_CreateNewChallenge extends WebTestCaseBase {

    @Test(groups = {"webCases"})
    public void verifyNewChallengeCreatedSuccessfully() throws AutomationException {
        ExtentManager.createTest("Verify new challenge created successfully");

        ExtentManager.log("Login to https://ctflearn.com.");
        int userNumber = 1;
        LoginPage loginPage = openCtfLearnApp();
        DashboardPage dashboardPage = loginPage.loginToDashboard(userNumber);

        ExtentManager.log("Navigate to Challenges → Click 'Create Challenge'.");
        dashboardPage.navigateToChallengesDropdownItem();
        NewChallengeInfo newChallengeInfo = new NewChallengeInfo();
        CreateChallengePage createChallengePage = dashboardPage.selectCreateChallengeDropdownItem();

        ExtentManager.log("Create a challenge.");
        createChallengePage.createNewChallenge(newChallengeInfo);

        ExtentManager.log("Open 'My Challenge' and verify that the created challenge is displayed correctly.");
        dashboardPage.navigateToProfileIcon();
        int firstCard = 1;
        ChallengeListPage challengeListPage = dashboardPage.clickOnMyChallengeItem();
        challengeListPage.validateChallengeCardInfoAtIndex(newChallengeInfo, firstCard);
        ChallengeDetailPage challengeDetailPage = challengeListPage.clickOnCardAtIndex(firstCard);
        challengeDetailPage.validateAllInfoInChallengeCard(newChallengeInfo);

        ExtentManager.log("Logout.");
        dashboardPage.navigateToProfileIcon();
        dashboardPage.clickOnLogoutItem();
    }
}
