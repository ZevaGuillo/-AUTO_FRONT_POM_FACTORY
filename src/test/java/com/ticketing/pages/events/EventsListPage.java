package com.ticketing.pages.events;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DefaultUrl("/")
public class EventsListPage extends BasePage {

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
        return eventCards.size();
    }

    /**
     * Find an event card by its title text and click "Select Seats"
     */
    public void selectEventByName(String eventName) {
        logger.info("Selecting event: {}", eventName);
        for (WebElementFacade card : eventCards) {
            WebElement title = card.findElement(By.cssSelector("h2"));
            if (title.getText().trim().equalsIgnoreCase(eventName)) {
                WebElement selectSeatsLink = card.findElement(By.cssSelector("a[href^='/events/']"));
                selectSeatsLink.click();
                return;
            }
        }
        throw new AssertionError("Event not found: " + eventName);
    }

    /**
     * Check if an event with the given name exists in the list
     */
    public boolean isEventVisible(String eventName) {
        for (WebElementFacade card : eventCards) {
            WebElement title = card.findElement(By.cssSelector("h2"));
            if (title.getText().trim().equalsIgnoreCase(eventName)) {
                return true;
            }
        }
        return false;
    }
}
