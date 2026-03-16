package com.ticketing.pages.publicpages;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Events listing page (Home page)
 * URL: /
 */
@DefaultUrl("/")
public class EventsPage extends BasePage {
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".event-card")
    private List<WebElementFacade> eventCards;
    
    @FindBy(css = ".event-card h3")
    private List<WebElementFacade> eventNames;
    
    @FindBy(css = ".event-card p")
    private List<WebElementFacade> eventDescriptions;
    
    @FindBy(css = "[data-event-date]")
    private List<WebElementFacade> eventDates;
    
    @FindBy(css = "[data-event-venue]")
    private List<WebElementFacade> eventVenues;
    
    @FindBy(css = ".event-card a")
    private List<WebElementFacade> eventLinks;
    
    @FindBy(css = ".navbar")
    private WebElementFacade navbar;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    /**
     * Get the page title text
     * @return Page title text
     */
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    /**
     * Get list of all event cards
     * @return List of event card elements
     */
    public List<WebElementFacade> getEventCards() {
        waitForPageLoad();
        return eventCards;
    }
    
    /**
     * Get count of events displayed
     * @return Number of events on the page
     */
    public int getEventCount() {
        return getEventCards().size();
    }
    
    /**
     * Click on an event by its index
     * @param index Index of the event to click (0-based)
     */
    public void clickEventByIndex(int index) {
        if (index < eventLinks.size()) {
            WebElementFacade eventLink = eventLinks.get(index);
            clickElement(eventLink);
        } else {
            throw new IllegalArgumentException("Event index " + index + " is out of range. Total events: " + eventLinks.size());
        }
    }
    
    /**
     * Click on the first available event
     */
    public void clickFirstEvent() {
        if (!eventLinks.isEmpty()) {
            clickElement(eventLinks.get(0));
        } else {
            throw new RuntimeException("No events available to click");
        }
    }
    
    /**
     * Get event name by index
     * @param index Index of the event
     * @return Event name text
     */
    public String getEventNameByIndex(int index) {
        if (index < eventNames.size()) {
            return getText(eventNames.get(index));
        }
        return "";
    }
    
    /**
     * Get event description by index
     * @param index Index of the event
     * @return Event description text
     */
    public String getEventDescriptionByIndex(int index) {
        if (index < eventDescriptions.size()) {
            return getText(eventDescriptions.get(index));
        }
        return "";
    }
    
    /**
     * Verify that events are displayed on the page
     * @return true if events are visible
     */
    public boolean areEventsDisplayed() {
        waitForPageLoad();
        return !eventCards.isEmpty() && eventCards.get(0).isVisible();
    }
    
    /**
     * Verify that the page has loaded completely
     * @return true if page is loaded
     */
    public boolean isPageLoaded() {
        try {
            waitForElement(pageTitle);
            return pageTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the navbar is visible
     * @return true if navbar is visible
     */
    public boolean isNavbarVisible() {
        return isElementVisible(navbar);
    }
    
    /**
     * Wait for events to load (loading spinner to disappear)
     */
    public void waitForEventsToLoad() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
    }
    
    /**
     * Verify all event cards have required information
     * @return true if all events have name, description, date, and venue
     */
    public boolean areAllEventsComplete() {
        if (eventCards.isEmpty()) return false;
        
        for (int i = 0; i < eventCards.size(); i++) {
            if (getEventNameByIndex(i).isEmpty() || 
                getEventDescriptionByIndex(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get all event names as a list
     * @return List of event name strings
     */
    public List<String> getAllEventNames() {
        return eventNames.stream()
                .map(this::getText)
                .toList();
    }
}