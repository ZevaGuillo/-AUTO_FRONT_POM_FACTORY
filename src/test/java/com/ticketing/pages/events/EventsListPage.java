package com.ticketing.pages.events;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@DefaultUrl("/")
public class EventsListPage extends BasePage {

    private static final int PAGE_TIMEOUT = 15;

    @FindBy(css = "h1")
    private WebElementFacade pageTitle;

    @FindBy(css = "[data-slot='card']")
    private List<WebElementFacade> eventCards;

    public boolean isEventsPageDisplayed() {
        try {
            return waitForElement(pageTitle).isDisplayed()
                && getText(pageTitle).contains("Events");
        } catch (Exception e) {
            return false;
        }
    }

    public int getEventCount() {
        waitForEventCards();
        return eventCards.size();
    }

    /**
     * Wait until at least one event card is rendered (async API fetch).
     */
    private void waitForEventCards() {
        logger.debug("Waiting for event cards to load");
        new WebDriverWait(getDriver(), Duration.ofSeconds(PAGE_TIMEOUT))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("[data-slot='card']")));
    }

    /**
     * Find an event card by its title text and click "Select Seats".
     * Waits for cards to render and for URL change after click.
     */
    public void selectEventByName(String eventName) {
        logger.info("Selecting event: {}", eventName);
        waitForEventCards();

        for (WebElementFacade card : eventCards) {
            WebElement title = card.findElement(By.cssSelector("h2"));
            if (title.getText().trim().equalsIgnoreCase(eventName)) {
                WebElement selectSeatsLink = card.findElement(
                        By.cssSelector("a[href^='/events/']"));
                selectSeatsLink.click();

                // Wait for SPA navigation: URL must change to /events/{uuid}
                new WebDriverWait(getDriver(), Duration.ofSeconds(PAGE_TIMEOUT))
                        .until(ExpectedConditions.urlContains("/events/"));
                logger.info("Navigated to: {}", getDriver().getCurrentUrl());
                return;
            }
        }
        throw new AssertionError("Event not found in list: " + eventName);
    }

    /**
     * Check if an event with the given name exists in the list
     */
    public boolean isEventVisible(String eventName) {
        waitForEventCards();
        for (WebElementFacade card : eventCards) {
            WebElement title = card.findElement(By.cssSelector("h2"));
            if (title.getText().trim().equalsIgnoreCase(eventName)) {
                return true;
            }
        }
        return false;
    }
}
