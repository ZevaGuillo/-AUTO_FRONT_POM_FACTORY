package com.ticketing.pages.publicpages;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Event Detail page with seat map
 * URL: /events/[eventId]
 */
@DefaultUrl("/events")
public class EventDetailPage extends BasePage {
    
    @FindBy(css = "[data-event-name]")
    private WebElementFacade eventName;
    
    @FindBy(css = "[data-event-description]")
    private WebElementFacade eventDescription;
    
    @FindBy(css = "[data-event-date]")
    private WebElementFacade eventDate;
    
    @FindBy(css = "[data-event-venue]")
    private WebElementFacade eventVenue;
    
    @FindBy(css = ".seatmap")
    private WebElementFacade seatmapContainer;
    
    @FindBy(css = ".seat-button.available")
    private List<WebElementFacade> availableSeats;
    
    @FindBy(css = ".seat-button.reserved")
    private List<WebElementFacade> reservedSeats;
    
    @FindBy(css = ".seat-button.selected")
    private List<WebElementFacade> selectedSeats;
    
    @FindBy(css = "[data-price]")
    private WebElementFacade seatPrice;
    
    @FindBy(css = "[data-add-to-cart]")
    private WebElementFacade addToCartButton;
    
    @FindBy(css = ".cart-sidebar")
    private WebElementFacade cartSidebar;
    
    @FindBy(css = "[data-cart-count]")
    private WebElementFacade cartItemCount;
    
    @FindBy(css = "[data-cart-total]")
    private WebElementFacade cartTotal;
    
    @FindBy(css = ".seat-legend")
    private WebElementFacade seatLegend;
    
    /**
     * Get the event name
     * @return Event name text
     */
    public String getEventName() {
        waitForElement(eventName);
        return getText(eventName);
    }
    
    /**
     * Get the event description
     * @return Event description text
     */
    public String getEventDescription() {
        waitForElement(eventDescription);
        return getText(eventDescription);
    }
    
    /**
     * Get the event date
     * @return Event date text
     */
    public String getEventDate() {
        waitForElement(eventDate);
        return getText(eventDate);
    }
    
    /**
     * Get the event venue
     * @return Event venue text
     */
    public String getEventVenue() {
        waitForElement(eventVenue);
        return getText(eventVenue);
    }
    
    /**
     * Check if the seatmap is displayed
     * @return true if seatmap is visible
     */
    public boolean isSeatmapDisplayed() {
        waitForElement(seatmapContainer);
        return isElementVisible(seatmapContainer);
    }
    
    /**
     * Get count of available seats
     * @return Number of available seats
     */
    public int getAvailableSeatsCount() {
        return availableSeats.size();
    }
    
    /**
     * Get count of reserved seats
     * @return Number of reserved seats
     */
    public int getReservedSeatsCount() {
        return reservedSeats.size();
    }
    
    /**
     * Get count of selected seats
     * @return Number of selected seats
     */
    public int getSelectedSeatsCount() {
        return selectedSeats.size();
    }
    
    /**
     * Select the first available seat
     */
    public void selectFirstAvailableSeat() {
        if (!availableSeats.isEmpty()) {
            WebElementFacade firstSeat = availableSeats.get(0);
            clickElement(firstSeat);
            logger.info("Selected first available seat");
        } else {
            throw new RuntimeException("No available seats to select");
        }
    }
    
    /**
     * Select an available seat by index
     * @param index Index of the seat to select (0-based)
     */
    public void selectAvailableSeatByIndex(int index) {
        if (index < availableSeats.size()) {
            WebElementFacade seat = availableSeats.get(index);
            clickElement(seat);
            logger.info("Selected available seat at index {}", index);
        } else {
            throw new IllegalArgumentException("Seat index " + index + " is out of range. Available seats: " + availableSeats.size());
        }
    }
    
    /**
     * Click Add to Cart button
     */
    public void clickAddToCart() {
        waitForElement(addToCartButton);
        clickElement(addToCartButton);
        logger.info("Clicked Add to Cart button");
    }
    
    /**
     * Check if Add to Cart button is enabled
     * @return true if button is enabled
     */
    public boolean isAddToCartEnabled() {
        waitForElement(addToCartButton);
        return addToCartButton.isEnabled();
    }
    
    /**
     * Get the seat price text
     * @return Seat price text
     */
    public String getSeatPrice() {
        if (isElementVisible(seatPrice)) {
            return getText(seatPrice);
        }
        return "";
    }
    
    /**
     * Check if cart sidebar is visible
     * @return true if cart sidebar is displayed
     */
    public boolean isCartSidebarVisible() {
        return isElementVisible(cartSidebar);
    }
    
    /**
     * Get cart item count
     * @return Cart item count text
     */
    public String getCartItemCount() {
        if (isElementVisible(cartItemCount)) {
            return getText(cartItemCount);
        }
        return "0";
    }
    
    /**
     * Get cart total amount
     * @return Cart total text
     */
    public String getCartTotal() {
        if (isElementVisible(cartTotal)) {
            return getText(cartTotal);
        }
        return "$0";
    }
    
    /**
     * Verify that event details are displayed
     * @return true if all event details are visible
     */
    public boolean areEventDetailsDisplayed() {
        return isElementVisible(eventName) && 
               isElementVisible(eventDescription) && 
               isElementVisible(eventDate) && 
               isElementVisible(eventVenue);
    }
    
    /**
     * Try to click on a reserved seat (should not work)
     * @return true if seat remains in reserved state
     */
    public boolean tryClickReservedSeat() {
        if (!reservedSeats.isEmpty()) {
            WebElementFacade reservedSeat = reservedSeats.get(0);
            int selectedCountBefore = getSelectedSeatsCount();
            clickElement(reservedSeat);
            int selectedCountAfter = getSelectedSeatsCount();
            return selectedCountBefore == selectedCountAfter; // Should remain the same
        }
        return true; // No reserved seats to test
    }
    
    /**
     * Verify seat legend is displayed
     * @return true if seat legend is visible
     */
    public boolean isSeatLegendDisplayed() {
        return isElementVisible(seatLegend);
    }
    
    /**
     * Wait for seatmap to load completely
     */
    public void waitForSeatmapToLoad() {
        waitForElement(seatmapContainer);
        waitForPageLoad();
        logger.info("Seatmap loaded successfully");
    }
}