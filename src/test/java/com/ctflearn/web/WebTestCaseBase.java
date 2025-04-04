package com.ctflearn.web;

import com.ctflearn.utils.ExtentManager;
import com.ctflearn.wrappers.WebTestWrapper;
import com.ctflearn.utils.ConfigManager;
import com.ctflearn.exception.AutomationException;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.ctflearn.pageobjects.web.LoginPage;

import java.io.File;

public abstract class WebTestCaseBase {

    protected WebTestWrapper webTestWrapper = new WebTestWrapper();;
    protected ConfigManager configManager = new ConfigManager();
    protected LoginPage loginPage;

    @BeforeSuite
    public void setupExtentReports() {
        ExtentManager.createInstance();
    }

    @BeforeMethod
    public void setUp() throws AutomationException {
        openCtfLearnApp();
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws AutomationException {
        String screenshotPath = null;
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = System.getProperty("user.dir") + "/test-output/screenshots";
            String fileName = result.getName() + ".png";
            screenshotPath = path + File.separator + fileName;
            webTestWrapper.captureWebDriverScreenshot(path, fileName);
        }
        ExtentManager.generateReport(screenshotPath);
        if (webTestWrapper != null) {
            webTestWrapper.closeWebBrowser();
        }
    }

    @AfterSuite
    public void flushReports() {
        ExtentManager.flushReports();
    }

    protected void openCtfLearnApp() throws AutomationException {
        webTestWrapper.initializeWebBrowser(configManager.getValueOfProperty("browser"), configManager.getValueOfProperty("baseUrl"));
        loginPage = new LoginPage(webTestWrapper);
    }

}
