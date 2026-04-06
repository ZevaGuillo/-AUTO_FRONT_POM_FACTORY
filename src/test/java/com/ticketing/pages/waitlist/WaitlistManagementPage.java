package com.ticketing.pages.waitlist;

import com.ticketing.pages.BasePage;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the "My Waitlist" management page (/waitlist).
 * Lists all waitlist subscriptions and allows cancellation.
 */
@DefaultUrl("/waitlist")
public class WaitlistManagementPage extends BasePage {

    // Page title: "My Waitlist"
    private static final String PAGE_TITLE_XPATH =
            "//h1[contains(text(),'My Waitlist')]";

    // Waitlist entry card
    private static final String ENTRY_CARD_XPATH =
            "//div[@data-slot='card']";

    // Card title (event name) inside a card
    private static final String CARD_TITLE_XPATH =
            ".//div[@data-slot='card-title']";

    // Card description (section name) inside a card
    private static final String CARD_DESCRIPTION_XPATH =
            ".//div[@data-slot='card-description']";

    // Cancel button (X icon with sr-only "Cancel waitlist")
    private static final String CANCEL_BUTTON_XPATH =
            ".//button[.//span[contains(text(),'Cancel waitlist')]]";

    // ========================================================================
    // Page load
    // ========================================================================

    public boolean isPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAGE_TITLE_XPATH)));
            return true;
        } catch (Exception e) {
            logger.debug("Waitlist management page not loaded");
            return false;
        }
    }

    // ========================================================================
    // Entry interactions
    // ========================================================================

    public List<WebElement> getAllEntryCards() {
        return getDriver().findElements(By.xpath(ENTRY_CARD_XPATH));
    }

    public boolean hasEntryForEvent(String eventName) {
        for (WebElement card : getAllEntryCards()) {
            try {
                String title = card.findElement(By.xpath(CARD_TITLE_XPATH)).getText();
                if (title.contains(eventName)) {
                    return true;
                }
            } catch (Exception e) {
                // skip malformed cards
            }
        }
        return false;
    }

    /**
     * Click the cancel (X) button on the waitlist card for a given event.
     */
    public void cancelEntryForEvent(String eventName) {
        logger.info("Cancelling waitlist entry for: {}", eventName);
        for (WebElement card : getAllEntryCards()) {
            try {
                String title = card.findElement(By.xpath(CARD_TITLE_XPATH)).getText();
                if (title.contains(eventName)) {
                    WebElement cancelBtn = card.findElement(By.xpath(CANCEL_BUTTON_XPATH));
                    ((org.openqa.selenium.JavascriptExecutor) getDriver())
                            .executeScript("arguments[0].scrollIntoView({block:'center'});", cancelBtn);
                    try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    cancelBtn.click();
                    return;
                }
            } catch (Exception e) {
                // skip
            }
        }
        throw new AssertionError("No waitlist entry found for event: " + eventName);
    }

    /**
     * After cancellation, verify the entry is gone (wait for DOM update).
     */
    public boolean waitForEntryToDisappear(String eventName) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(driver -> !hasEntryForEvent(eventName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
