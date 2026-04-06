package com.ticketing.pages.events;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Event Details / Seat Selection page.
 * URL pattern: /events/{eventId}
 * Handles event info, section headers, seat map, and seat selection panel.
 */
public class EventDetailsPage extends BasePage {

    // Event info
    @FindBy(css = "h1.text-3xl")
    private WebElementFacade eventTitle;

    @FindBy(css = "a[href='/']")
    private WebElementFacade backToEventsLink;

    // Section headers: h3 with class text-accent uppercase
    private static final String SECTION_HEADER_CSS = "h3.text-sm.font-semibold.text-accent";

    // Seat buttons: all seats use aria-label pattern "Seat {Section}{Row}-{Num}, ${Price}, {Status}"
    private static final String ALL_SEATS_CSS = "button[aria-label^='Seat']";

    // Seat selection info panel (appears when a seat is clicked)
    @FindBy(css = "div.rounded-lg.border.border-accent\\/30")
    private WebElementFacade seatInfoPanel;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-sm.font-medium")
    private WebElementFacade seatInfoText;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-lg")
    private WebElementFacade seatPriceText;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-xs.text-amber-600")
    private WebElementFacade seatReservedWarning;

    /**
     * Waits for the event details page to fully render.
     * Checks that the URL matches /events/{uuid} and that the h1
     * contains actual event text (not the stale "Events" from the list page).
     */
    public boolean isEventDetailsPageLoaded() {
        try {
            // 1. Wait for URL pattern
            new org.openqa.selenium.support.ui.WebDriverWait(
                    getDriver(), java.time.Duration.ofSeconds(15))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions
                            .urlMatches(".*/events/[0-9a-fA-F-]+$"));

            // 2. Wait for h1 to be visible
            waitForElement(eventTitle);

            // 3. Ensure it's NOT the stale list-page h1
            String titleText = eventTitle.getText().trim();
            if ("Events".equalsIgnoreCase(titleText) || titleText.isEmpty()) {
                // SPA hasn't finished rendering — retry once after short delay
                Thread.sleep(1000);
                titleText = eventTitle.getText().trim();
            }

            boolean loaded = !titleText.isEmpty()
                    && !"Events".equalsIgnoreCase(titleText);
            logger.info("Event details page loaded: {} (title='{}')", loaded, titleText);
            return loaded;
        } catch (Exception e) {
            logger.error("Event details page failed to load", e);
            return false;
        }
    }

    public String getEventName() {
        return getText(eventTitle);
    }

    public void clickBackToEvents() {
        clickElement(backToEventsLink);
    }

    /**
     * Get all section header names displayed on the page.
     * Section headers look like "Section General", "Section Sección 2"
     */
    public List<WebElement> getSectionHeaders() {
        return getDriver().findElements(By.cssSelector(SECTION_HEADER_CSS));
    }

    /**
     * Check if a section with the given name exists.
     * The HTML renders "Section {name}" as the h3 text.
     */
    public boolean isSectionVisible(String sectionName) {
        for (WebElement header : getSectionHeaders()) {
            if (header.getText().toUpperCase().contains(sectionName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a seat button by its aria-label containing the given text.
     * Example: findSeatByAriaLabel("General1-4") to locate seat 4 in row 1 of General section.
     */
    public WebElement findSeatByAriaLabel(String ariaLabelContains) {
        String xpath = String.format("//button[contains(@aria-label, '%s')]", ariaLabelContains);
        return waitForElement(By.xpath(xpath));
    }

    /**
     * Click a specific seat by section, row, and seat number.
     * @param section Section name (e.g. "General")
     * @param row Row number
     * @param seat Seat number
     */
    public void clickSeat(String section, int row, int seat) {
        String ariaFragment = String.format("%s%d-%d", section, row, seat);
        logger.info("Clicking seat with aria-label containing: {}", ariaFragment);
        WebElement seatButton = findSeatByAriaLabel(ariaFragment);
        seatButton.click();
    }

    /**
     * Find all seats in a section that have a specific status (Available, Reserved, Sold).
     */
    public List<WebElement> findSeatsByStatus(String section, String status) {
        String xpath = String.format("//button[contains(@aria-label, '%s') and contains(@aria-label, '%s')]",
                section, status);
        return getDriver().findElements(By.xpath(xpath));
    }

    /**
     * Click the first reserved seat in a given section to trigger the waitlist panel.
     * Uses JS click to bypass overlapping elements after page refresh.
     */
    public void clickFirstReservedSeat(String section) {
        logger.info("Clicking first reserved seat in section: {}", section);
        List<WebElement> reservedSeats = findSeatsByStatus(section, "Reserved");
        if (reservedSeats.isEmpty()) {
            throw new AssertionError("No reserved seats found in section: " + section);
        }
        WebElement seat = reservedSeats.get(0);
        ((org.openqa.selenium.JavascriptExecutor) getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", seat);
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        ((org.openqa.selenium.JavascriptExecutor) getDriver())
                .executeScript("arguments[0].click();", seat);
    }

    /**
     * Click the first seat in a given section regardless of status.
     * Used when interacting with section to explore seat map / trigger UI.
     */
    public void clickFirstSeatInSection(String section) {
        logger.info("Clicking first seat in section: {}", section);
        String xpath = String.format("//button[contains(@aria-label, 'Seat %s')]", section);
        List<WebElement> seats = getDriver().findElements(By.xpath(xpath));
        if (seats.isEmpty()) {
            throw new AssertionError("No seats found in section: " + section);
        }
        seats.get(0).click();
    }

    /**
     * Check if the seat info panel is visible (appears after clicking a seat).
     */
    public boolean isSeatInfoPanelVisible() {
        try {
            return waitForElement(seatInfoPanel).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the seat info text (e.g. "Section General, Row 1, Seat 4").
     */
    public String getSeatInfoText() {
        return getText(seatInfoText);
    }

    /**
     * Get the seat price text (e.g. "$50.00").
     */
    public String getSeatPrice() {
        return getText(seatPriceText);
    }

    /**
     * Check if the "Seat reserved by another user" warning is visible.
     */
    public boolean isReservedWarningVisible() {
        try {
            return isElementVisible(seatReservedWarning);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if a given section has any available seats.
     */
    public boolean sectionHasAvailableSeats(String section) {
        return !findSeatsByStatus(section, "Available").isEmpty();
    }

    /**
     * Check if a given section has any reserved seats.
     */
    public boolean sectionHasReservedSeats(String section) {
        return !findSeatsByStatus(section, "Reserved").isEmpty();
    }

    // ========================================================================
    // Seat reservation ("Reserve & Add to Cart" button)
    // ========================================================================

    private static final String RESERVE_BUTTON_XPATH =
            "//button[contains(text(), 'Reserve') and contains(text(), 'Add to Cart')]";

    /**
     * After clicking a seat, the info panel appears with "Reserve & Add to Cart".
     * Clicks that button to reserve the selected seat.
     */
    public void clickReserveAndAddToCart() {
        logger.info("Clicking 'Reserve & Add to Cart' button");
        WebElement btn = waitForElement(By.xpath(RESERVE_BUTTON_XPATH));
        btn.click();
    }

    /**
     * Whether the "Reserve & Add to Cart" button is visible.
     */
    public boolean isReserveButtonVisible() {
        try {
            return !getDriver().findElements(By.xpath(RESERVE_BUTTON_XPATH)).isEmpty()
                    && getDriver().findElement(By.xpath(RESERVE_BUTTON_XPATH)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
