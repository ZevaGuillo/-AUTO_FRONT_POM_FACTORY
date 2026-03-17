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

/**
 * BasePage class containing common functionality for all Page Objects.
 * Extends Serenity PageObject to inherit WebDriver management and reporting capabilities.
 */
public abstract class BasePage extends PageObject {
    
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 5;
    
    /**
     * Wait for an element to be visible on the page
     * @param locator The By locator for the element
     * @return WebElement once it's visible
     */
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element with locator: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for a WebElementFacade to be visible
     * @param element The WebElementFacade to wait for
     * @return The same WebElementFacade once visible
     */
    protected WebElementFacade waitForElement(WebElementFacade element) {
        logger.debug("Waiting for WebElementFacade to be visible");
        element.waitUntilVisible();
        return element;
    }
    
    /**
     * Click an element after ensuring it's clickable
     * @param element The element to click
     */
    protected void clickElement(WebElementFacade element) {
        logger.debug("Clicking element: {}", element);
        waitForElement(element);
        element.waitUntilClickable();
        element.click();
    }
    
    /**
     * Get text from an element after ensuring it's visible
     * @param element The element to get text from
     * @return The text content of the element
     */
    protected String getText(WebElementFacade element) {
        logger.debug("Getting text from element");
        waitForElement(element);
        return element.getText().trim();
    }
    
    /**
     * Check if an element is visible on the page
     * @param element The element to check
     * @return true if visible, false otherwise
     */
    protected boolean isElementVisible(WebElementFacade element) {
        try {
            return element.isVisible();
        } catch (Exception e) {
            logger.debug("Element not visible: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Type text into an input field
     * @param element The input element
     * @param text The text to type
     */
    protected void typeText(WebElementFacade element, String text) {
        logger.debug("Typing text '{}' into element", text);
        waitForElement(element);
        element.clear();
        element.type(text);
    }
    
    /**
     * Wait for an element to be present in the DOM
     * @param locator The By locator for the element
     * @return WebElement once it's present
     */
    protected WebElement waitForElementPresent(By locator) {
        logger.debug("Waiting for element to be present with locator: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for an element to be clickable
     * @param element The element to wait for
     * @return The same element once clickable
     */
    protected WebElementFacade waitForElementClickable(WebElementFacade element) {
        logger.debug("Waiting for element to be clickable");
        element.waitUntilClickable();
        return element;
    }
    
    /**
     * Get current page URL
     * @return Current page URL
     */
    protected String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    /**
     * Get current page title
     * @return Current page title
     */
    protected String getPageTitle() {
        return getDriver().getTitle();
    }
    
    /**
     * Scroll to element
     * @param element The element to scroll to
     */
    protected void scrollToElement(WebElementFacade element) {
        logger.debug("Scrolling to element");
        evaluateJavascript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    
    /**
     * Wait for page to load completely
     */
    protected void waitForPageLoad() {
        logger.debug("Waiting for page to load completely");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(driver -> evaluateJavascript("return document.readyState").equals("complete"));
    }
}