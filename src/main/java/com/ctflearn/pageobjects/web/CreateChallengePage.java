package com.ctflearn.pageobjects.web;

import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.constants.ToastMessageConstant;
import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.dataobjects.NewChallengeInfo;

public class CreateChallengePage extends AbstractWebPage {

    private static final Logger logger = LogManager.getLogger(CreateChallengePage.class.getSimpleName());

    public CreateChallengePage(WebTestWrapper webTestWrapper) {
        super(webTestWrapper);
    }

    private final static String eventDropdown = "//select[@id='event_id']";
    private final static String titleInput = "//input[@id='title']";
    private final static String flagInput = "//input[@id='flag']";
    private final static String descriptionTextarea = "//textarea[@id='flask-pagedown-description']";
    private final static String categoryDropdown = "//select[@id='category']";
    private final static String pointsDropdown = "//select[@id='points']";
    private final static String howToSolveTextarea = "//textarea[@id='howtosolve']";
    private final static String submitButton = "//button[@type='submit']";

    public void selectEvent(String event) throws AutomationException {
        logger.info("Select event: {}", event);
        webTestWrapper.clickControl(eventDropdown);
    }

    public void enterTitle(String title) throws AutomationException {
        logger.info("Enter title: {}", title);
        webTestWrapper.clearText(titleInput);
        webTestWrapper.inputText(titleInput, title);
    }

    public void enterFlag(String flag) throws AutomationException {
        logger.info("Enter flag: {}", flag);
        webTestWrapper.clearText(flagInput);
        webTestWrapper.inputText(flagInput, flag);
    }

    public void enterDescription(String description) throws AutomationException {
        logger.info("Enter description: {}", description);
        webTestWrapper.clearText(descriptionTextarea);
        webTestWrapper.inputText(descriptionTextarea, description);
    }

    public void attachFile(String file) throws AutomationException {
        logger.info("Attach file: {}", file);
        uploadFile(file);
    }

    public void selectCategory(String category) throws AutomationException {
        logger.info("Select category: {}", category);
        webTestWrapper.selectItemInCombobox(categoryDropdown, category);
    }

    public void selectPoints(String points) throws AutomationException {
        logger.info("Select points: {}", points);
        webTestWrapper.selectItemInCombobox(pointsDropdown, points);
    }

    public void enterHowtoSolve(String content) throws AutomationException {
        logger.info("Enter HowToSolve: {}", content);
        webTestWrapper.inputText(howToSolveTextarea, content);
    }

    public void clickOnSubmitButton() throws AutomationException {
        logger.info("Click on Submit button");
        webTestWrapper.clickControl(submitButton);
    }

    public void createNewChallenge(NewChallengeInfo newChallengeInfo) throws AutomationException {
        selectEvent(newChallengeInfo.getEvent());
        enterTitle(newChallengeInfo.getTitle());
        enterFlag(newChallengeInfo.getFlag());
        enterDescription(newChallengeInfo.getDescription());
        selectCategory(newChallengeInfo.getCategory());
        selectPoints(newChallengeInfo.getPoints());
        enterHowtoSolve(newChallengeInfo.getContent());
        clickOnSubmitButton();
        verifyToastMessage(ToastMessageConstant.SUBMIT_FOR_VERIFICATION_SUCCESS.getName());
    }

}
