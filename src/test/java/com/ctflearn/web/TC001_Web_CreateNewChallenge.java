package com.ctflearn.web;

import com.aventstack.extentreports.Status;
import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.ExtentManager;
import org.testng.annotations.Test;
import com.ctflearn.dataobjects.NewChallengeInfo;
import com.ctflearn.pageobjects.web.ChallengeDetailPage;
import com.ctflearn.pageobjects.web.ChallengeListPage;
import com.ctflearn.pageobjects.web.CreateChallengePage;
import com.ctflearn.pageobjects.web.DashboardPage;

public class TC001_Web_CreateNewChallenge extends WebTestCaseBase {

    @Test
    public void verifyNewChallengeCreatedSuccessfully() throws AutomationException {
        ExtentManager.createTest("Verify new challenge created successfully");

        int userNumber = 1;
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
