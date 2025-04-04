package com.ctflearn.wrappers;

import com.ctflearn.constants.Constants;
import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.ExtentManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ctflearn.utils.CmdHelper;
import com.ctflearn.utils.CommonUtil;
import com.ctflearn.utils.ConfigManager;

import java.io.File;
import java.time.Duration;
import java.util.*;

public class MobileTestWrapper {

    private static final Logger logger = LogManager.getLogger(MobileTestWrapper.class.getSimpleName());
    private final DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
    protected AppiumDriver driver;
    private ConfigManager configManager = new ConfigManager();
    private AppiumDriverLocalService appiumService;
    private MobileType mobileType;

    public void initializeMobileDriver() throws AutomationException {
        int count = 0;
        int maxRetries = 1;
        mobileType = MobileType.valueOf(configManager.getValueOfProperty("mobilePlatform").toUpperCase());
        logger.info("Starting a new '{}' Driver", mobileType);

        while (count <= maxRetries) {
            configureCapabilities();
            printAppiumStatus();
            logger.debug(" >> Initialize {} Driver ...", mobileType);
            try {
                switch (mobileType) {
                    case ANDROID:
                        driver = new AndroidDriver(appiumService.getUrl(), capabilities);
                        driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 1);
                        break;
                    case IOS:
                        driver = new IOSDriver(appiumService.getUrl(), capabilities);
                        driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 1);
                        break;
                    default:
                        throw new AutomationException("Can't initialize Driver! Mobile type does not define yet.");
                }
                break;
            } catch (SessionNotCreatedException e) {
                if (count != maxRetries) {
                    count++;
                    logger.error("Launch mobile failed. Trying to launch mobile in the time {}", count);
                    logger.error(e.getMessage());
                } else {
                    throw new AutomationException(e.getMessage());
                }
            }
        }
    }

    /**
     * Configure capabilities.
     *
     * @throws AutomationException the automation exception
     */
    public void configureCapabilities() throws AutomationException {
        capabilities.setCapability("autoLaunch", true);
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("dataReset", true);
        capabilities.setCapability("autoGrantPermissions", true);
        switch (mobileType) {
            case ANDROID:
                startAppium();
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("uiautomator2ServerInstallTimeout", "60000");
                capabilities.setCapability("uiautomator2ServerLaunchTimeout", "60000");
                capabilities.setCapability("androidInstallTimeout", "120000");
                capabilities.setCapability("adbExecTimeout", "60000");
                capabilities.setCapability("automationName", "UiAutomator2");
                capabilities.setCapability("deviceName", configManager.getValueOfProperty("deviceName"));
                capabilities.setCapability("app", configManager.getValueOfProperty("app"));
                capabilities.setCapability("appPackage", configManager.getValueOfProperty("appPackage"));
                capabilities.setCapability("appActivity", configManager.getValueOfProperty("appActivity"));
                break;
            case IOS:
            default:
                throw new AutomationException("The '%s' device type does not support", mobileType);
        }
    }

    public void printAppiumStatus() {
        logger.info("=============================================================");
        logger.debug("Thread ID : {}", Thread.currentThread().getId());
        logger.debug("URL : {}{}", appiumService.getUrl(), "");
        Map<String, Object> capas = capabilities.toJson();
        for (Map.Entry<String, Object> entry : capas.entrySet()) {
            if (!entry.getKey().equals("securityToken")) {
                logger.info("capabilities ({}): [{}]", entry.getKey(), entry.getValue());
            }
        }
        logger.info("=============================================================");
    }

    public void startAppium() throws AutomationException {
        String nodePath = CmdHelper.executeCommandAndWaitForOutput("npm root -g").trim();

        logger.info("Starting a new Appium service");

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.usingAnyFreePort();
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.RELAXED_SECURITY);
        builder.withAppiumJS(new File(nodePath + "/appium/build/lib/main.js"));

        appiumService = AppiumDriverLocalService.buildService(builder);
        if (isAppiumServerRunning()) {
            stopAppium();
        }

        logger.debug(" >> Appium is starting...");
        appiumService.start();
        logger.info("Appium service started with URL {}.", appiumService.getUrl());
    }
    
    public void stopAppium() throws AutomationException {
        try {
            logger.info("Stopping Appium service with URL {}.", appiumService.getUrl());
            appiumService.stop();
            logger.info("Appium service stopped");
        } catch (NullPointerException e) {
            logger.debug("Can't Stop Appium. The service is null");
        }
    }

    public boolean isAppiumServerRunning() throws AutomationException {
        return appiumService.isRunning();
    }

    public void closeMobileDriver() {
        logger.info("Mobile driver is closing ...");
        try {
            driver.quit();
            logger.debug(" >> Closed.");
        } catch (NullPointerException e) {
            logger.error(" >> Can't close driver. The Driver is null!");
        }
    }

    private WebElement getElement(String control, long waitingTime) throws AutomationException {
        return getElement(control, waitingTime, null);
    }

    private WebElement getElement(String control, long waitingTime, ElementStateEnum expectedCondition) throws AutomationException {
        WebElement WebElement = waitForElementCondition(control, waitingTime, expectedCondition);
        switch (mobileType) {
            case IOS:
                List<WebElement> WebElementList = getElements(control, waitingTime);
                logger.debug(" >> the total element is '{}'", WebElementList.size());
                WebElement = WebElementList.get(WebElementList.size() - 1);
                break;
            case ANDROID:
                break;
            default:
        }
        return WebElement;
    }

    public List<WebElement> getElements(String control, long waitingTime) throws AutomationException {
        List<WebElement> elementList = null;
        boolean unFound = true;
        long tries = 0;

        while (unFound && tries < waitingTime) {
            tries += 1;
            try {
                By by = getBy(control);
                elementList = driver.findElements(by);
                if (!elementList.isEmpty()) {
                    unFound = false;
                }
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                logControlException(control, e.getClass().getSimpleName(), e.getMessage());
            } catch (Exception e) {
                logControlException(control, e.getClass().getSimpleName(), e.getMessage());
            }
            CommonUtil.sleepSeconds(1);

        }

        if (unFound) {
            throw new AutomationException(String.format("Failed to find element '%s' after %d retries", control, tries));
        }

        return elementList;
    }

    private By getBy(String control) throws AutomationException {
        if (control == null) {
            throw new AutomationException("The control text is NULL!");
        }

        String controlUpdated = "";
        switch (mobileType) {
            case ANDROID:
                controlUpdated = control.replace("@name", "@content-desc").replace("@label", "@resource-id");
                break;
            case IOS:
                controlUpdated = control.replace("@content-desc", "@name").replace("@resource-id", "@label");
                break;
            default:
        }
        return control.startsWith("//") ? By.xpath(controlUpdated) : By.cssSelector(control);
    }

    protected WebElement waitForElementCondition(String control, long waitingTime, ElementStateEnum expectedConditionsMethodName) throws AutomationException {
        if (expectedConditionsMethodName == null) {
            expectedConditionsMethodName = ElementStateEnum.PRESENT;
        }

        return waitForElementMeetCondition(control, waitingTime, expectedConditionsMethodName);
    }

    private WebElement waitUntil(String control, long waitingTime, ElementStateEnum expectedConditionsMethodName) throws AutomationException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitingTime));
        switch (expectedConditionsMethodName) {
            case PRESENT:
                return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(getBy(control)));
            case VISIBLE:
                return (WebElement) wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(control)));
            case SELECTED:
                if (Boolean.TRUE.equals(wait.until(ExpectedConditions.elementToBeSelected(getBy(control))))) {
                    return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(getBy(control)));
                }
                return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(getBy(control)));
            case CLICKABLE:
                return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(getBy(control)));
            default:
                throw new AutomationException("The expected condition is not supported: " + expectedConditionsMethodName);
        }
    }

    private WebElement waitForElementMeetCondition(String control, long waitingTime, ElementStateEnum expectedConditionsMethodName) throws AutomationException {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1L));
        wait.ignoring(StaleElementReferenceException.class);

        //wait timeout occurs 10 times of waitingTime. So, the method will run between 25-150 seconds
        while ((System.currentTimeMillis() - startTime) < (waitingTime * 1000)) {
            logger.debug("Searching for element {}: '{}.'", expectedConditionsMethodName, control);
            try {
                element = waitUntil(control, waitingTime, expectedConditionsMethodName);
                found = true;
                break;
            } catch (StaleElementReferenceException e) {
                logControlException(control, e.getClass().getSimpleName(), e.getMessage());
            } catch (TimeoutException te) {
                found = false;
            } catch (Exception e) {
                throw new AutomationException(e);
            }
            CommonUtil.sleepSeconds(1);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        if (!found) {
            throw new AutomationException(String.format("Element '%s' not found after %d seconds.", control, totalTime / 1000));
        }

        logger.debug("Element '{}' is found within {} seconds.", control, (totalTime / 1000 + 1));
        return element;
    }

    public String getText(WebElement element) throws AutomationException {
        String text = "";

        int startCount = 0;
        int retryCount = 3;
        while (startCount < retryCount) {
            try {
                text = element.getText();
                if (!text.isEmpty()) {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                logger.info("ERROR: [Get Text] Stale element: \n {} \n", e.getMessage());
                startCount++;
            }
        }

        if (startCount == retryCount) {
            throw new AutomationException("Maximum number of retries is reached: {%s}", retryCount);
        }

        return text;
    }

    public boolean isElementPresentNoErrorThrown(String element, long waitingTime) {
        try {
            ExtentManager.setError(false);
            getElement(element, waitingTime);
            ExtentManager.setError(true);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
        return true;
    }

    public void isElementPresent(String element, long waitingTime) throws AutomationException {
        if (isElementPresentNoErrorThrown(element, waitingTime)) {
            logger.debug("Element '{}' was found within {} seconds.", element, waitingTime);
        } else {
            throw new AutomationException(String.format("Element '%s' was not found within %s seconds.", element, waitingTime));
        }
    }

    public void clickControl(String control) throws AutomationException {
        clickControlWithRetry(control, false, Constants.WAITING_TIME_DEFAULT, ElementStateEnum.CLICKABLE);
    }

    private void clickControlWithRetry(String control, boolean scrollIntoView, long waitingTime, ElementStateEnum expectedCondition) throws AutomationException {
        int startCount = 0;
        int retryCount = 3;
        boolean scrollingElements = false;
        boolean scrollUpDown = true;
        boolean tryScrolling = true;
        WebElement element;
        while (startCount < retryCount) {
            try {
                element = getElement(control, waitingTime, expectedCondition);
                element.click();
                logger.debug("Click control '{}'.", control);
                startCount = retryCount;
            } catch (Exception e) {
                if (e instanceof StaleElementReferenceException) {
                    logControlException(control, e.getClass().getSimpleName(), e.getMessage());
                    logger.info("Re-try to click control '{}'.", control);
                    startCount++;
                } else if (e.getMessage().contains("Other element would receive the click") && !scrollingElements) {
                    logControlException(control, e.getClass().getSimpleName(), e.getMessage());
                    logger.info("Re-try once to click control '{}'.", control);
                    scrollingElements = true;
                    CommonUtil.sleepSeconds(3);
                    startCount = 0; // reset the startCount for StaleElementReferenceException
                } else if ((e.getMessage().contains("Element is not clickable at point")) && tryScrolling) {
                    logControlException(control, e.getClass().getSimpleName(), e.getMessage());
                    logger.info("Element is outside the view area");
                    startCount = 0; // reset the startCount for StaleElementReferenceException
                    scrollUpDown = !scrollUpDown;//first time click after aligning to bottom
                    if (scrollUpDown) {
                        tryScrolling = false;
                    }
                } else {
                    logger.error("Control not found: {}", control);
                    throw new AutomationException(e);
                }
            }
        }
    }

    public void inputText(String control, String text) throws AutomationException {
        inputTextWithRetry(control, text, Constants.WAITING_TIME_DEFAULT, false);
    }

    private void inputTextWithRetry(String control, String text, long waitingTime, boolean exactly) throws AutomationException {
        int startCount = 0;
        int retryCount = 3;

        while (startCount < retryCount) {
            try {
                WebElement WebElement = getElement(control, waitingTime);
                WebElement.sendKeys(text);

                String textInTextBox = getText(WebElement);

                if (textInTextBox != null) //for some areas textInTextBox is always null
                {
                    if (!textInTextBox.equals(text) && exactly) {
                        logger.debug("Text input '{}' does not match given text: '{}'", textInTextBox, text);
                        startCount++;
                        continue;
                    } else {
                        logger.debug("The text in the text-box is properly typed: {}", textInTextBox);
                    }
                }

                logger.debug("Inputted text '{}' in the text-box '{}'.", text, control);
                startCount = retryCount;
            } catch (InvalidElementStateException | StaleElementReferenceException e) {
                //catch InvalidElementStateException: invalid element state: Element is not currently interactable and may not be manipulated
                logControlException(control, e.getClass().getSimpleName(), e.getMessage());
                logger.info("Re-try to input text in control '{}'.", control);
                startCount++;
            }

        }
    }

    private void logControlException(String control, String exceptionName, String exceptionMessage) {
        logger.info("{}: {} \n {}", exceptionName, control, exceptionMessage);
    }

    public void captureMobileDriverScreenshot(String path, String filename) throws AutomationException {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(path + File.separator + filename));
            logger.debug("A screenshot of the current web driver has been captured. ");
        } catch (Exception e) {
            throw new AutomationException("%s Unable to capture screenshot. %s", e);
        }
    }

    private enum ElementStateEnum {
        CLICKABLE("elementToBeClickable"),
        SELECTED("elementToBeSelected"),
        PRESENT("presenceOfElementLocated"),
        VISIBLE("visibilityOfElementLocated");

        @Getter
        private final String state;

        ElementStateEnum(String state) {
            this.state = state;
        }
    }

    private enum MobileType {
        ANDROID,
        IOS
    }
}
