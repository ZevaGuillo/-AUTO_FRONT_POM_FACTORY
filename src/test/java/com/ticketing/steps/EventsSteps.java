package com.ticketing.steps;

import com.ticketing.pages.publicpages.EventDetailPage;
import com.ticketing.pages.publicpages.EventsPage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.annotations.Step;

/**
 * Steps class for Events related actions
 * Acts as orchestrator between StepDefinitions and PageObjects
 */
public class EventsSteps {
    
    private EventsPage eventsPage;
    private EventDetailPage eventDetailPage;
    
    @Step("Navigate to the home page")
    public void navigateToHome() {
        eventsPage.open();
        eventsPage.waitForEventsToLoad();
        TestUtils.logTestStep("Navigated to home page");
    }
    
    @Step("Navigate to the events page")
    public void navigateToEvents() {
        eventsPage.open();
        eventsPage.waitForEventsToLoad();
        TestUtils.logTestStep("Navigated to events page");
    }
    
    @Step("Verify events are displayed on the homepage")
    public boolean verifyEventsDisplayed() {
        boolean eventsDisplayed = eventsPage.areEventsDisplayed();
        TestUtils.logTestStep("Verified events displayed: " + eventsDisplayed);
        return eventsDisplayed;
    }
    
    @Step("Verify page has loaded completely")
    public boolean verifyPageLoaded() {
        boolean pageLoaded = eventsPage.isPageLoaded();
        TestUtils.logTestStep("Verified page loaded: " + pageLoaded);
        return pageLoaded;
    }
    
    @Step("Get the page title")
    public String getPageTitle() {
        String title = eventsPage.getPageTitle();
        TestUtils.logTestStep("Got page title: " + title);
        return title;
    }
    
    @Step("Get the number of events displayed")
    public int getEventCount() {
        int count = eventsPage.getEventCount();
        TestUtils.logTestStep("Got event count: " + count);
        return count;
    }
    
    @Step("Click on the first event")
    public void clickFirstEvent() {
        eventsPage.clickFirstEvent();
        TestUtils.logTestStep("Clicked on first event");
    }
    
    @Step("Click on event by index: {0}")
    public void clickEventByIndex(int index) {
        eventsPage.clickEventByIndex(index);
        TestUtils.logTestStep("Clicked on event at index: " + index);
    }
    
    @Step("Verify all events have complete information")
    public boolean verifyAllEventsComplete() {
        boolean allComplete = eventsPage.areAllEventsComplete();
        TestUtils.logTestStep("Verified all events complete: " + allComplete);
        return allComplete;
    }
    
    @Step("Get all event names")
    public java.util.List<String> getAllEventNames() {
        java.util.List<String> eventNames = eventsPage.getAllEventNames();
        TestUtils.logTestStep("Retrieved all event names: " + eventNames.size() + " events");
        return eventNames;
    }
    
    @Step("Verify navbar is visible")
    public boolean verifyNavbarVisible() {
        boolean navbarVisible = eventsPage.isNavbarVisible();
        TestUtils.logTestStep("Verified navbar visible: " + navbarVisible);
        return navbarVisible;
    }
    
    // Event Detail Page Steps
    
    @Step("Verify event detail page is loaded")
    public boolean verifyEventDetailPageLoaded() {
        boolean detailsDisplayed = eventDetailPage.areEventDetailsDisplayed();
        TestUtils.logTestStep("Verified event detail page loaded: " + detailsDisplayed);
        return detailsDisplayed;
    }
    
    @Step("Get event name from detail page")
    public String getEventNameFromDetail() {
        String eventName = eventDetailPage.getEventName();
        TestUtils.logTestStep("Got event name from detail: " + eventName);
        return eventName;
    }
    
    @Step("Get event description from detail page")
    public String getEventDescriptionFromDetail() {
        String description = eventDetailPage.getEventDescription();
        TestUtils.logTestStep("Got event description from detail page");
        return description;
    }
    
    @Step("Get event date from detail page")
    public String getEventDateFromDetail() {
        String eventDate = eventDetailPage.getEventDate();
        TestUtils.logTestStep("Got event date from detail: " + eventDate);
        return eventDate;
    }
    
    @Step("Get event venue from detail page")
    public String getEventVenueFromDetail() {
        String venue = eventDetailPage.getEventVenue();
        TestUtils.logTestStep("Got event venue from detail: " + venue);
        return venue;
    }
    
    @Step("Verify seatmap is displayed")
    public boolean verifySeatmapDisplayed() {
        eventDetailPage.waitForSeatmapToLoad();
        boolean seatmapDisplayed = eventDetailPage.isSeatmapDisplayed();
        TestUtils.logTestStep("Verified seatmap displayed: " + seatmapDisplayed);
        return seatmapDisplayed;
    }
    
    @Step("Get available seats count")
    public int getAvailableSeatsCount() {
        int availableSeats = eventDetailPage.getAvailableSeatsCount();
        TestUtils.logTestStep("Got available seats count: " + availableSeats);
        return availableSeats;
    }
    
    @Step("Get reserved seats count")
    public int getReservedSeatsCount() {
        int reservedSeats = eventDetailPage.getReservedSeatsCount();
        TestUtils.logTestStep("Got reserved seats count: " + reservedSeats);
        return reservedSeats;
    }
    
    @Step("Get selected seats count")
    public int getSelectedSeatsCount() {
        int selectedSeats = eventDetailPage.getSelectedSeatsCount();
        TestUtils.logTestStep("Got selected seats count: " + selectedSeats);
        return selectedSeats;
    }
    
    @Step("Select first available seat")
    public void selectFirstAvailableSeat() {
        eventDetailPage.selectFirstAvailableSeat();
        TestUtils.logTestStep("Selected first available seat");
    }
    
    @Step("Select available seat by index: {0}")
    public void selectAvailableSeatByIndex(int index) {
        eventDetailPage.selectAvailableSeatByIndex(index);
        TestUtils.logTestStep("Selected available seat at index: " + index);
    }
    
    @Step("Click Add to Cart button")
    public void clickAddToCart() {
        eventDetailPage.clickAddToCart();
        TestUtils.logTestStep("Clicked Add to Cart button");
    }
    
    @Step("Verify Add to Cart button is enabled")
    public boolean verifyAddToCartEnabled() {
        boolean enabled = eventDetailPage.isAddToCartEnabled();
        TestUtils.logTestStep("Verified Add to Cart enabled: " + enabled);
        return enabled;
    }
    
    @Step("Get seat price")
    public String getSeatPrice() {
        String price = eventDetailPage.getSeatPrice();
        TestUtils.logTestStep("Got seat price: " + price);
        return price;
    }
    
    @Step("Verify cart sidebar is visible")
    public boolean verifyCartSidebarVisible() {
        boolean cartVisible = eventDetailPage.isCartSidebarVisible();
        TestUtils.logTestStep("Verified cart sidebar visible: " + cartVisible);
        return cartVisible;
    }
    
    @Step("Get cart item count")
    public String getCartItemCount() {
        String count = eventDetailPage.getCartItemCount();
        TestUtils.logTestStep("Got cart item count: " + count);
        return count;
    }
    
    @Step("Get cart total")
    public String getCartTotal() {
        String total = eventDetailPage.getCartTotal();
        TestUtils.logTestStep("Got cart total: " + total);
        return total;
    }
    
    @Step("Try to click reserved seat")
    public boolean tryClickReservedSeat() {
        boolean remainsReserved = eventDetailPage.tryClickReservedSeat();
        TestUtils.logTestStep("Tried to click reserved seat, remains reserved: " + remainsReserved);
        return remainsReserved;
    }
    
    @Step("Verify seat legend is displayed")
    public boolean verifySeatLegendDisplayed() {
        boolean legendDisplayed = eventDetailPage.isSeatLegendDisplayed();
        TestUtils.logTestStep("Verified seat legend displayed: " + legendDisplayed);
        return legendDisplayed;
    }
    
    @Step("Complete seat selection and add to cart")
    public void completeSeatSelectionAndAddToCart() {
        selectFirstAvailableSeat();
        TestUtils.waitSeconds(1); // Small wait for UI update
        clickAddToCart();
        TestUtils.logTestStep("Completed seat selection and added to cart");
    }
    
    @Step("Verify event has required details: name, description, date, venue")
    public boolean verifyEventHasRequiredDetails() {
        boolean hasName = !getEventNameFromDetail().isEmpty();
        boolean hasDescription = !getEventDescriptionFromDetail().isEmpty();
        boolean hasDate = !getEventDateFromDetail().isEmpty();
        boolean hasVenue = !getEventVenueFromDetail().isEmpty();
        
        boolean allPresent = hasName && hasDescription && hasDate && hasVenue;
        TestUtils.logTestStep("Verified event has all required details: " + allPresent);
        return allPresent;
    }
}