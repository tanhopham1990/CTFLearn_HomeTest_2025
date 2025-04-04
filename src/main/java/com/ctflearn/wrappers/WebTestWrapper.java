package com.ctflearn.wrappers;

import com.ctflearn.constants.Constants;
import com.ctflearn.exception.AutomationException;
import com.ctflearn.utils.CommonUtil;
import com.ctflearn.utils.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

public class WebTestWrapper {

    private static final Logger logger = LogManager.getLogger(WebTestWrapper.class.getSimpleName());

    private static final String ELEMENT_TO_BE_CLICKABLE = "elementToBeClickable";
    private static final String ERROR_LOST_REFERENCE_CONTROL = "Lost reference to control: '{}'.";
    private static final String ERROR_MAXIMUM_NUMBER_OF_RETRIES_IS_REACHED = "Maximum number of retries is reached: {%s}";
    private static final String ATTRIBUTE_INNER_TEXT = "innerText";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String XPATH_SELECT = "Select";

    @Getter
    protected WebDriver driver;
    private BrowserType browserType;

    public void initializeWebBrowser(String type, String url) throws AutomationException {
        logger.info("Initializing New Web browser: [{}].", type);
        logger.info("Url : {}", url);

        browserType = BrowserType.valueOf(type.toUpperCase());
        initializeDriverWithRetry();
        startWebBrowser(url);
    }

    public void closeWebBrowser() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("Web browser is closed.");
    }

    private void startWebBrowser(String url) {
        driver.manage().window().maximize();
        driver.get(url);
    }

    private void initializeDriverWithRetry() throws AutomationException {
        logger.info("initialize {} driver ....", browserType);
        int startCount = 0;
        while (startCount < 3) {
            logger.info("Count = {}", startCount);
            try {
                initializeDriver();
                startCount = 3;
            } catch (Exception e) {
                logger.error(e);
                if (startCount < 2) {
                    startCount++;
                } else {
                    throw new AutomationException("Initialize web driver {} failed.", e, browserType);
                }
            }
        }
    }

    private void initializeDriver() {
        switch (browserType) {
            case CHROME:
                initializeChromeDriver(false, false, "");
                break;
            case CHROME_HEADLESS:
                initializeChromeDriver(true, false, "");
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case IE:
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
                break;
        }
        logger.info("complete {} ({}) driver initialization.", browserType, ((RemoteWebDriver) driver).getCapabilities().getBrowserVersion());
    }

    private void initializeChromeDriver(boolean isHeadless, boolean isCustomDownloadPath, String downloadFolderPath) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (isHeadless) {
            logger.info("Chrome will start with HEADLESS mode");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }
        if (isCustomDownloadPath) {
            logger.info("Downloads will be saved into: {} ", downloadFolderPath);
            HashMap<String, Object> setPath = new HashMap<>();
            setPath.put("download.default_directory", downloadFolderPath); //to set path
            options.setExperimentalOption("prefs", setPath);
        }
        driver = new ChromeDriver(options);
    }

    private List<WebElement> getAllRows(WebElement tableEle, boolean includeNestedTableRows) {
        if (includeNestedTableRows) {
            return tableEle.findElements(By.xpath(".//tr"));
        } else {
            return tableEle.findElements(By.xpath("tbody/tr | tr"));
        }
    }

    private String trimText(String text) {
        return text.replace("\\<.*?>", "").replace("\u00A0", "").replace("\n", "").replace("\r", "").replace("&amp;", "&").replace("&nbsp;", "").replace("&gt;", ">");
    }

    public void clickElementByIndex(String listControl, int index) throws AutomationException {
        clickElementByIndex(listControl, index, Constants.WAITING_TIME_DEFAULT);
    }

    public void clickElementByIndex(String listControl, int index, long waitingTime) throws AutomationException {
        getElement(listControl, waitingTime);
        List<WebElement> listElements = driver.findElements(By.xpath(listControl));
        listElements.get(index - 1).click();
    }

    //handle for combobox, tag name <select>
    public void selectItemInCombobox(String control, String itemName) throws AutomationException {
        WebElement combobox = getElement(control, Constants.WAITING_TIME_SHORT);
        Select select = new Select(combobox);
        select.selectByVisibleText(itemName);
    }
    //End handle for context menu

    public void uploadFile(String xpath, String filepath) throws AutomationException {
        logger.debug("Upload file: {}", filepath);
        File file = new File(filepath);

        if (!file.exists()) {
            throw new AutomationException("File does not exist.");
        }

        WebElement browseButton = driver.findElement(By.xpath(xpath));
        browseButton.sendKeys(file.getAbsolutePath());
    }

    public List<WebElement> getAllElementInList(String listControl) throws AutomationException {
        logger.debug("Get all Elements in list: '{}'", listControl);
        return driver.findElements(By.xpath(listControl));
    }

    private WebElement getElement(String control, long waitingTime) throws AutomationException {
        return getElement(control, waitingTime, null);
    }

    private WebElement getElement(String control, long waitingTime, String expectedConditionMethodName) throws AutomationException {
        return waitForElementCondition(control, waitingTime, expectedConditionMethodName);
    }

    private By by(String control) {
        return control.startsWith("//") || control.startsWith("(//") ? By.xpath(control) : By.cssSelector(control);
    }

    public String getValue(String control) throws AutomationException {
        String text = "";
        int startCount = 0;
        int retryCount = 3;
        while (startCount < retryCount) {
            try {
                WebElement element = getElement(control, Constants.WAITING_TIME_DEFAULT);

                if (element.getTagName().equals(XPATH_SELECT)) {
                    Select selectElement = new Select(element);
                    if (!selectElement.isMultiple()) {
                        text = selectElement.getFirstSelectedOption().getText();
                    } else {
                        throw new AutomationException("Multiple select element is not supported");
                    }
                } else if ((element.getTagName().equals("span")) || (element.getTagName().equals("div"))) {
                    text = element.getText();
                } else if (element.getTagName().equals("input")) {
                    String type = element.getAttribute("type");

                    if (type.equals("text")) {
                        text = element.getAttribute(ATTRIBUTE_VALUE);
                    } else if ((type.equals("checkbox")) || (type.equals("radio"))) {
                        if (element.getAttribute("checked").equalsIgnoreCase("true")) {
                            text = "checked";
                        } else {
                            text = "unchecked";
                        }
                    } else if (element.getAttribute(ATTRIBUTE_VALUE) != null) {
                        text = element.getAttribute(ATTRIBUTE_VALUE);
                    } else {
                        text = element.getText();
                    }
                } else if (element.getTagName().equals("textarea")) {
                    text = element.getAttribute(ATTRIBUTE_VALUE);
                } else {
                    text = element.getText();
                }
                logger.debug("The value of control '{}' = '{}'.", control, text);
                break;
            } catch (StaleElementReferenceException e) {
                logger.debug("[Get Value] Stale element: \n {} \n", e.getMessage());
                logger.debug(ERROR_LOST_REFERENCE_CONTROL, control);
                startCount++;
            } catch (AutomationException e) {
                // Element is not found - Exception from getWebElement
                logger.error("[Get Value] Unable to get element value '{}'.", control);
                throw e;
            }
        }

        if (startCount == retryCount) {
            throw new AutomationException(ERROR_MAXIMUM_NUMBER_OF_RETRIES_IS_REACHED, retryCount);
        }

        return text;
    }


    public String getText(String control) throws AutomationException {
        String text = "";

        int startCount = 0;
        int retryCount = 3;
        while (startCount < retryCount) {
            try {
                WebElement element = getElement(control, Constants.WAITING_TIME_DEFAULT);
                text = trimText(element.getAttribute(ATTRIBUTE_INNER_TEXT));
                break;
            } catch (StaleElementReferenceException e) {
                logger.info("[Get Text] Stale element: \n {} \n", e.getMessage());
                logger.info(ERROR_LOST_REFERENCE_CONTROL, control);
                startCount++;
            } catch (AutomationException e) {
                // Element is not found - Exception from getWebElement
                logger.error("[Get Text] Unable to get element value '{}'.", control, e);
                throw e;
            }
        }
        if (startCount == retryCount) {
            throw new AutomationException(ERROR_MAXIMUM_NUMBER_OF_RETRIES_IS_REACHED, retryCount);
        }
        return text;
    }

    protected WebElement waitForElementCondition(String control, long waitingTime, String expectedConditionsMethodName) throws AutomationException {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement webElement = null;
        int maxTimeout = 91000;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitingTime));
        wait.ignoring(StaleElementReferenceException.class);

        //wait timeout occurs 3 times within the 90 seconds limit.  So, the method will run between 15-90 seconds
        while ((System.currentTimeMillis() - startTime) < maxTimeout) {
            boolean loop = false;
            logger.debug("Searching for element: '{}.'", control);
            try {
                webElement = wait.until(ExpectedConditions.presenceOfElementLocated(by(control)));
                found = true;
                loop = true;
            } catch (StaleElementReferenceException e) {
                logger.info("[Wait For Element Condition] Stale element: \n {} \n", e.getMessage());
            } catch (TimeoutException te) {
                loop = true;
            }

            if (loop) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        if (!found) {
            throw new AutomationException(String.format("Element '%s' not found after %d seconds.", control, totalTime / 1000));
        }
        logger.debug("Element '{}' is found within {} seconds.", control, (totalTime / 1000 + 1));

        if (expectedConditionsMethodName != null) {
            Method expectedConditionsMethod;
            try {
                expectedConditionsMethod = ExpectedConditions.class.getMethod(expectedConditionsMethodName, By.class);
            } catch (NoSuchMethodException e) {
                throw new AutomationException(e);
            }

            boolean conditionMet = false;
            final long conditionStartTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - conditionStartTime) < maxTimeout) {
                logger.debug("Waiting for element '{}' condition: '{}'", control, expectedConditionsMethodName);
                try {
                    if (expectedConditionsMethod.getGenericReturnType().getTypeName().equals("org.openqa.selenium.support.ui.ExpectedCondition<java.lang.Boolean>")) //boolean type
                    {
                        wait.until((ExpectedCondition<Boolean>) expectedConditionsMethod.invoke(ExpectedConditions.class, by(control)));
                    } else {
                        webElement = wait.until((ExpectedCondition<WebElement>) expectedConditionsMethod.invoke(ExpectedConditions.class, by(control)));
                    }
                    conditionMet = true;
                    break;
                } catch (StaleElementReferenceException e) {
                    logger.debug("[Wait For Element Condition] Stale element: \n {} \n", e.getMessage());
                } catch (TimeoutException te) {
                    logger.debug("[Wait For Element Condition] TimeoutException: \n {} \n", te.getMessage());
                    break;
                } catch (Exception e) {
                    logger.error("[Wait For Element Condition] Exception: \n {} \n", e.getMessage());
                    throw new AutomationException(e);
                }
            }
            long conditionEndTime = System.currentTimeMillis();
            long conditionTotalTime = conditionEndTime - conditionStartTime;
            if (!conditionMet) {
                throw new AutomationException(String.format("Element '%s' condition '%s' is not met after %d seconds", control, expectedConditionsMethodName, conditionTotalTime / 1000));
            }
            logger.debug("Element '{}' condition '{}' is met within {} seconds", control, expectedConditionsMethodName, conditionTotalTime / 1000 + 1);
        }
        return webElement;
    }

    public void clearText(String control) throws AutomationException {
        clearText(control, Constants.WAITING_TIME_DEFAULT);
    }

    public void clearText(String control, long waitingTime) throws AutomationException {
        WebElement element = getElement(control, waitingTime);
        String operationSystem = System.getProperty("os.name");
        Keys key;
        if (operationSystem.contains("Mac")) {
            key = Keys.COMMAND;
        } else {
            key = Keys.CONTROL;
        }
        element.sendKeys(key + "a");
        element.sendKeys(Keys.DELETE);
    }

    private void scrollIntoView(WebElement control) {
        logger.debug("------scrolling into view---------");
        Actions actions = new Actions(driver);
        actions.moveToElement(control);
        actions.perform();
    }

    public boolean isElementPresentNoErrorThrown(String element, long waitingTime) {
        try {
            ExtentManager.setError(false);
            getElement(element, waitingTime, null);
            ExtentManager.setError(true);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
        return true;
    }

    public void isElementPresent(String element) throws AutomationException {
        isElementPresent(element, Constants.WAITING_TIME_DEFAULT);
    }

    public void isElementPresent(String element, long waitingTime) throws AutomationException {
        if (isElementPresentNoErrorThrown(element, waitingTime)) {
            logger.debug("Element '{}' is present.", element);
        } else {
            logger.debug("Element '{}' is not present.", element);
            throw new AutomationException(String.format("Element '%s' is not present.", element));
        }
    }

    public void typeKey(String control, Keys keys, long waitingTime) throws AutomationException {
        logger.info("Type '{}' key on control '{}", keys, control);
        getElement(control, waitingTime).sendKeys(keys);
    }

    public void clickControl(String control) throws AutomationException {
        clickControl(control, false, Constants.WAITING_TIME_DEFAULT);
    }

    public void clickControl(String control, long waitingTime) throws AutomationException {
        clickControl(control, false, waitingTime);
    }

    public void clickControl(String control, boolean scrollIntoView, long waitingTime) throws AutomationException {
        clickControlWithRetry(control, scrollIntoView, waitingTime);
    }

    private void clickControlWithRetry(String control, boolean scrollIntoView, long waitingTime) throws AutomationException {
        int startCount = 0;
        int retryCount = 3;
        boolean scrollingElements = false;
        boolean scrollUpDown = true; //true = scroll to the top of the scrolling area
        boolean tryScrolling = true;
        while (startCount < retryCount) {
            WebElement webElement = getElement(control, waitingTime);
            try {
                if (scrollIntoView) {
                    scrollIntoView(webElement);
                }
                webElement = waitForElementCondition(control, waitingTime, ELEMENT_TO_BE_CLICKABLE);
                webElement.click();
                logger.debug("Click control '{}'.", control);
                startCount = retryCount;
            } catch (Exception e) {
                //catch StaleElementReferenceException in case of refresh
                if (e instanceof StaleElementReferenceException) {
                    logger.info("[Click control with retry] Stale element: \n {} \n", e.getMessage());
                    logger.info("Re-try to click control '{}'.", control);
                    startCount++;
                }
                //catch unknown error when the window elements appear with scrolling (e.g. Configure RAR application)
                else if (e.getMessage().contains("Other element would receive the click") && !scrollingElements) {
                    logger.error(e.getMessage());
                    logger.info("Re-try once to click control '{}'.", control);
                    scrollingElements = true;
                    CommonUtil.sleepSeconds((int) Constants.WAITING_TIME_DEFAULT);
                    startCount = 0; // reset the startCount for StaleElementReferenceException
                } else if (e instanceof ElementClickInterceptedException) {
                    logger.info("Intercepted element: \n {} \n", e.getMessage());
                    CommonUtil.sleepSeconds((int) Constants.WAITING_TIME_DEFAULT);
                    webElement.click();
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
                typeEachCharacterFromText(control, text, waitingTime);

                String textInTextBox = getValue(control);

                if (textInTextBox != null) //for some areas textInTextBox is always null
                {
                    if (!textInTextBox.equals(text) && exactly) {
                        logger.debug("Text input '{}' does not match given text: '{}'", textInTextBox, text);
                        startCount++;
                    } else {
                        logger.debug("The text in the text-box is properly typed: {}", textInTextBox);
                    }
                }

                logger.debug("Input text '{}' in the text-box '{}'.", text, control);
                startCount = retryCount;
            } catch (InvalidElementStateException e) //catch InvalidElementStateException: invalid element state: Element is not currently interactable and may not be manipulated
            {
                logger.info("ERROR: {} \n", e.getMessage());
                logger.info("Re-try to input text in control '{}'.", control);
                startCount++;
                clickControl(control, waitingTime);
            } catch (StaleElementReferenceException e) //catch StaleElementReferenceException in case of refresh
            {
                logger.info("[Input Text With Retry] Stale element: \n {} \n", e.getMessage());
                logger.info("Re-try to input text in control '{}'.", control);
                startCount++;
            }
        }
    }

    private void typeEachCharacterFromText(String control, String text, long waitingTime) throws AutomationException {
        WebElement element = null;
        Actions actions = null;
        if (control == null) {
            actions = new Actions(driver);
        } else {
            element = getElement(control, waitingTime);
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String s = String.valueOf(c);
            if (element == null) {
                if (actions == null) {
                    actions = new Actions(driver);
                }
                actions.sendKeys(s).perform();
            } else {
                element.sendKeys(s);
            }
            logger.debug(">> {}", s);
            CommonUtil.sleepMilliseconds(50);
        }
    }

    public void captureWebDriverScreenshot(String path, String filename) throws AutomationException {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(path + File.separator + filename));
            logger.debug("A screenshot of the current web driver has been captured. ");
        } catch (Exception e) {
            throw new AutomationException("Unable to capture screenshot. %s", e);
        }
    }

    private enum BrowserType {
        CHROME,
        CHROME_HEADLESS,
        FIREFOX,
        IE
    }
}
