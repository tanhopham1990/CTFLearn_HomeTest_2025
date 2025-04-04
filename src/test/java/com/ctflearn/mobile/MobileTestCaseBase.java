package com.ctflearn.mobile;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.ctflearn.pageobjects.mobile.DashboardMobilePage;
import com.ctflearn.wrappers.MobileTestWrapper;

import java.io.File;

public abstract class MobileTestCaseBase {

    private static final Logger logger = LogManager.getLogger(MobileTestCaseBase.class.getSimpleName());

    protected MobileTestWrapper mobileTestWrapper = new MobileTestWrapper();;

    @BeforeSuite(alwaysRun = true)
    public void setupExtentReports() {
        ExtentManager.createInstance();
    }

    @BeforeMethod
    public void setUp() throws AutomationException {
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws AutomationException {
        String screenshotPath = null;
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = System.getProperty("user.dir") + "/test-output/screenshots";
            String fileName = result.getName() + ".png";
            screenshotPath = path + File.separator + fileName;
            mobileTestWrapper.captureMobileDriverScreenshot(path, fileName);
        }
//        ExtentManager.generateReport(screenshotPath);
        if (mobileTestWrapper != null) {
            mobileTestWrapper.closeMobileDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReports() {
        ExtentManager.flushReports();
    }

    protected DashboardMobilePage openTradingMobileApp() throws AutomationException {
        mobileTestWrapper.initializeMobileDriver();
        return new DashboardMobilePage(mobileTestWrapper);
    }

}
