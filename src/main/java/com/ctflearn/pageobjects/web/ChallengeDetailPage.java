package com.ctflearn.pageobjects.web;

import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.dataobjects.NewChallengeInfo;

public class ChallengeDetailPage extends AbstractWebPage {
    private static final Logger logger = LogManager.getLogger(ChallengeDetailPage.class.getSimpleName());

    public ChallengeDetailPage(WebTestWrapper webTestWrapper) {
        super(webTestWrapper);
    }

    private static String title = "//span[@id='title-display']";
    private static String description = "//div[@id='description-display']/p";
    private static String point = "//span[@id='points-display']";
    private static String category = "//span[@id='category-display']";

    public void validateAllInfoInChallengeCard(NewChallengeInfo newChallengeInfo) throws AutomationException {
        validateTextEquals(webTestWrapper.getText(title), newChallengeInfo.getTitle());
        validateTextEquals(webTestWrapper.getText(description), newChallengeInfo.getDescription());
        validateTextEquals(webTestWrapper.getText(point), newChallengeInfo.getPoints());
        validateTextEquals(webTestWrapper.getText(category), newChallengeInfo.getCategory());
    }
}
