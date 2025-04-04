package com.ctflearn.pageobjects.web;

import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import com.ctflearn.dataobjects.NewChallengeInfo;

import java.util.List;

public class ChallengeListPage extends AbstractWebPage {
    private static final Logger logger = LogManager.getLogger(ChallengeListPage.class.getSimpleName());

    public ChallengeListPage(WebTestWrapper webTestWrapper) {
        super(webTestWrapper);
    }

    private static String cards = "//div[contains(@class,'challenge-card')]";
    private static String titles = "//div[contains(@class,'challenge-card')]/div[contains(@class,'card-header')]/span";
    private static String points = "//div[contains(@class,'challenge-card')]/div[contains(@class,'card-body')]//span[@class='font-weight-bolder']";
    private static String categories = "//span[@id='category-display']";

    public ChallengeDetailPage clickOnCardAtIndex(int index) throws AutomationException {
        webTestWrapper.clickElementByIndex(cards, index);
        return new ChallengeDetailPage(webTestWrapper);
    }

    public void validateChallengeCardInfoAtIndex(NewChallengeInfo newChallengeInfo, int index) throws AutomationException {
        List<WebElement> titleControls = webTestWrapper.getAllElementInList(titles);
        List<WebElement> pointControls = webTestWrapper.getAllElementInList(points);
        List<WebElement> categoryControls = webTestWrapper.getAllElementInList(categories);

        validateTextEquals(titleControls.get(index - 1).getText(), newChallengeInfo.getTitle());
        validateTextEquals(pointControls.get(index - 1).getText(), newChallengeInfo.getPoints());
        validateTextEquals(categoryControls.get(index - 1).getText(), newChallengeInfo.getCategory());
    }

}
