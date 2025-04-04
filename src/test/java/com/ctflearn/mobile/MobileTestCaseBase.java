package com.ctflearn.mobile;

import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import com.ctflearn.pageobjects.mobile.DashboardMobilePage;
import com.ctflearn.wrappers.MobileTestWrapper;
import org.testng.annotations.BeforeSuite;

import java.io.File;

public class MobileTestCaseBase {

    private static final Logger logger = LogManager.getLogger(MobileTestCaseBase.class.getSimpleName());

    protected MobileTestWrapper mobileTestWrapper = new MobileTestWrapper();;
    protected DashboardMobilePage dashboardMobilePage;

    @BeforeSuite
    public void setupExtentReports() {
        ExtentManager.createInstance();
    }

    @BeforeMethod
    public void setUp() throws AutomationException {
        openTradingMobileApp();
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws AutomationException {
        String screenshotPath = null;
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = System.getProperty("user.dir") + "/test-output/screenshots";
            String fileName = result.getName() + ".png";
            screenshotPath = path + File.separator + fileName;
            mobileTestWrapper.captureMobileDriverScreenshot(path, fileName);
        }
        ExtentManager.generateReport(screenshotPath);
        if (mobileTestWrapper != null) {
            mobileTestWrapper.closeMobileDriver();
        }
    }

    @AfterSuite
    public void flushReports() {
        ExtentManager.flushReports();
    }

    protected void openTradingMobileApp() throws AutomationException {
        mobileTestWrapper.initializeMobileDriver();
        dashboardMobilePage = new DashboardMobilePage(mobileTestWrapper);
    }

}
