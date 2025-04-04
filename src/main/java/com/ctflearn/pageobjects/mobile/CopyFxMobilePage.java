package com.ctflearn.pageobjects.mobile;

import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ctflearn.wrappers.MobileTestWrapper;

public class CopyFxMobilePage extends AbstractMobilePage {

    private static final Logger logger = LogManager.getLogger(CopyFxMobilePage.class.getSimpleName());

    private static String portfolio = "//*[@content-desc=\"1\"]";

    public CopyFxMobilePage(MobileTestWrapper mobileTestWrapper) {
        super(mobileTestWrapper);
    }

    public void verifyPortfolioDisplays() throws AutomationException {
        logger.info("Verify portfolio displays");
        mobileTestWrapper.isElementPresent(portfolio, 3);
    }
}
