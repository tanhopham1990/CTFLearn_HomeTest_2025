package com.ctflearn.pageobjects.mobile;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.wrappers.MobileTestWrapper;

public class DashboardMobilePage extends AbstractMobilePage {

    public DashboardMobilePage(MobileTestWrapper mobileTestWrapper) throws AutomationException {
        super(mobileTestWrapper);
        if (mobileTestWrapper.isElementPresentNoErrorThrown(skipButton, 3)) {
            tapOnSkipButton();
        }
    }
}
