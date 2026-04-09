package com.ticketing.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage extends PageObject {
    
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 5;
    
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element with locator: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    protected WebElementFacade waitForElement(WebElementFacade element) {
        logger.debug("Waiting for WebElementFacade to be visible");
        element.waitUntilVisible();
        return element;
    }
    
    protected void clickElement(WebElementFacade element) {
        logger.debug("Clicking element: {}", element);
        waitForElement(element);
        element.waitUntilClickable();
        element.click();
    }
    
    protected String getText(WebElementFacade element) {
        logger.debug("Getting text from element");
        waitForElement(element);
        return element.getText().trim();
    }
    
    protected boolean isElementVisible(WebElementFacade element) {
        try {
            return element.isVisible();
        } catch (Exception e) {
            logger.debug("Element not visible: {}", e.getMessage());
            return false;
        }
    }
    
    protected void typeText(WebElementFacade element, String text) {
        logger.debug("Typing text '{}' into element", text);
        waitForElement(element);
        element.clear();
        element.type(text);
    }
    
    protected WebElement waitForElementPresent(By locator) {
        logger.debug("Waiting for element to be present with locator: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    protected WebElementFacade waitForElementClickable(WebElementFacade element) {
        logger.debug("Waiting for element to be clickable");
        element.waitUntilClickable();
        return element;
    }
    
    protected String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    protected String getPageTitle() {
        return getDriver().getTitle();
    }
    
    protected void scrollToElement(WebElementFacade element) {
        logger.debug("Scrolling to element");
        evaluateJavascript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    
    protected void waitForPageLoad() {
        logger.debug("Waiting for page to load completely");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(driver -> evaluateJavascript("return document.readyState").equals("complete"));
    }
}
