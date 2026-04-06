package com.ticketing.steps;

import com.ticketing.pages.user.LoginPage;
import com.ticketing.pages.events.EventsListPage;
import com.ticketing.pages.events.EventDetailsPage;
import com.ticketing.pages.waitlist.WaitlistPage;
import com.ticketing.pages.waitlist.WaitlistManagementPage;
import net.serenitybdd.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitlistSteps {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistSteps.class);
    private LoginPage loginPage;
    private EventsListPage eventsListPage;
    private EventDetailsPage eventDetailsPage;
    private WaitlistPage waitlistPage;
    private WaitlistManagementPage waitlistManagementPage;

    // ========================================================================
    // Authentication
    // ========================================================================

    @Step("Logging in as user {0}")
    public void loginAsUser(String email, String password) {
        logger.info("Logging in as user: {}", email);
        loginPage.open();
        loginPage.loginAs(email, password);
    }

    // ========================================================================
    // Navigation
    // ========================================================================

    @Step("Navigating to events list and selecting event: {0}")
    public void navigateToEventDetails(String eventName) {
        logger.info("Navigating to event: {}", eventName);
        eventsListPage.open();
        eventsListPage.selectEventByName(eventName);

        if (!eventDetailsPage.isEventDetailsPageLoaded()) {
            throw new AssertionError("Event details page failed to load for: " + eventName);
        }
    }

    @Step("Verifying event details page is loaded for: {0}")
    public boolean verifyEventDetailsLoaded(String eventName) {
        return eventDetailsPage.isEventDetailsPageLoaded()
            && eventDetailsPage.getEventName().contains(eventName);
    }

    // ========================================================================
    // Section & Seat interactions
    // ========================================================================

    @Step("Verifying section {0} is visible")
    public boolean verifySectionVisible(String sectionName) {
        return eventDetailsPage.isSectionVisible(sectionName);
    }

    @Step("Checking if section {0} has available seats")
    public boolean sectionHasAvailableSeats(String sectionName) {
        return eventDetailsPage.sectionHasAvailableSeats(sectionName);
    }

    @Step("Clicking first reserved seat in section: {0}")
    public void clickReservedSeatInSection(String sectionName) {
        logger.info("Clicking first reserved seat in section: {}", sectionName);
        eventDetailsPage.clickFirstReservedSeat(sectionName);
    }

    @Step("Clicking first seat in section: {0}")
    public void clickFirstSeatInSection(String sectionName) {
        logger.info("Clicking first seat in section: {}", sectionName);
        eventDetailsPage.clickFirstSeatInSection(sectionName);
    }

    // ========================================================================
    // Seat reservation (User A flow)
    // ========================================================================

    @Step("Clicking seat {0} row {1} seat {2}")
    public void clickSpecificSeat(String section, int row, int seat) {
        logger.info("Clicking seat: {} row {} seat {}", section, row, seat);
        eventDetailsPage.clickSeat(section, row, seat);
    }

    @Step("Clicking 'Reserve & Add to Cart' button")
    public void clickReserveAndAddToCart() {
        logger.info("Clicking Reserve & Add to Cart");
        eventDetailsPage.clickReserveAndAddToCart();
    }

    @Step("Clicking reserved seat in section: {0}")
    public void clickReservedSeat(String sectionName) {
        logger.info("Clicking reserved seat in section: {}", sectionName);
        eventDetailsPage.clickFirstReservedSeat(sectionName);
    }

    // ========================================================================
    // Waitlist — Join
    // ========================================================================

    @Step("Verifying Join Waitlist button is visible")
    public boolean verifyJoinWaitlistButtonVisible() {
        boolean isVisible = waitlistPage.isJoinWaitlistButtonVisible();
        logger.debug("Join Waitlist button visible: {}", isVisible);
        return isVisible;
    }

    @Step("Verifying Join Waitlist button is NOT visible")
    public boolean verifyJoinWaitlistButtonNotVisible() {
        return !waitlistPage.isJoinWaitlistButtonVisible();
    }

    @Step("Clicking Join Waitlist button")
    public void clickJoinWaitlistButton() {
        logger.info("Clicking Join Waitlist button");
        waitlistPage.clickJoinWaitlistButton();
    }

    // ========================================================================
    // Waitlist Banner
    // ========================================================================

    @Step("Verifying waitlist banner is visible")
    public boolean verifyWaitlistBannerVisible() {
        return waitlistPage.isWaitlistBannerVisible();
    }

    @Step("Verifying waitlist banner title contains: {0}")
    public boolean verifyWaitlistBannerTitle(String expectedText) {
        String title = waitlistPage.getWaitlistBannerTitle();
        logger.debug("Banner title: {}", title);
        return title.contains(expectedText);
    }

    @Step("Verifying waitlist banner disappeared")
    public boolean verifyWaitlistBannerDisappeared() {
        return !waitlistPage.isWaitlistBannerVisible();
    }

    // ========================================================================
    // Duplicate error
    // ========================================================================

    @Step("Verifying duplicate error message is visible")
    public boolean verifyDuplicateErrorVisible() {
        return waitlistPage.isDuplicateErrorVisible();
    }

    // ========================================================================
    // Toast notifications
    // ========================================================================

    @Step("Verifying toast notification with message: {0}")
    public boolean verifySuccessToast(String expectedMessage) {
        if (!waitlistPage.isToastVisible()) {
            logger.error("Toast notification not visible");
            return false;
        }
        String actualMessage = waitlistPage.getToastMessage();
        logger.debug("Toast message: {}", actualMessage);
        return actualMessage.contains(expectedMessage);
    }

    // ========================================================================
    // Waitlist Management Page (/waitlist)
    // ========================================================================

    @Step("Navigating to My Waitlist page")
    public void navigateToWaitlistPage() {
        logger.info("Navigating to /waitlist");
        waitlistManagementPage.open();
        if (!waitlistManagementPage.isPageLoaded()) {
            throw new AssertionError("My Waitlist page failed to load");
        }
    }

    @Step("Verifying waitlist entry exists for event: {0}")
    public boolean verifyWaitlistEntryExists(String eventName) {
        return waitlistManagementPage.hasEntryForEvent(eventName);
    }

    @Step("Cancelling waitlist entry for event: {0}")
    public void cancelWaitlistEntry(String eventName) {
        logger.info("Cancelling waitlist entry for: {}", eventName);
        waitlistManagementPage.cancelEntryForEvent(eventName);
    }

    @Step("Verifying waitlist entry disappeared for event: {0}")
    public boolean verifyWaitlistEntryDisappeared(String eventName) {
        return waitlistManagementPage.waitForEntryToDisappear(eventName);
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    @Step("Waiting for page update")
    public void waitForPageUpdate() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.warn("Interrupted during page update wait", e);
            Thread.currentThread().interrupt();
        }
    }

    @Step("Refreshing page")
    public void refreshPage() {
        eventDetailsPage.getDriver().navigate().refresh();
    }
}
