package com.ticketing.steps;

import com.ticketing.pages.user.LoginPage;
import com.ticketing.pages.events.EventsListPage;
import com.ticketing.pages.events.EventDetailsPage;
import com.ticketing.pages.waitlist.WaitlistPage;
import net.serenitybdd.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitlistSteps {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistSteps.class);
    private LoginPage loginPage;
    private EventsListPage eventsListPage;
    private EventDetailsPage eventDetailsPage;
    private WaitlistPage waitlistPage;

    @Step("Creating event {0} with section {1} fully reserved")
    public void createEventWithFullyReservedSection(String eventName, String sectionName) {
        logger.info("Creating event {} with section {} fully reserved", eventName, sectionName);
        
        String eventId = com.ticketing.utils.EventApiClient.getEventId(eventName);
        logger.info("Event ID from API: {}", eventId);
        if (eventId == null) {
            eventId = com.ticketing.utils.EventApiClient.createEvent(
                eventName,
                "Evento para pruebas de waitlist",
                java.time.LocalDateTime.now().plusDays(1).toString(),
                "Teatro Central",
                "1",
                "1.00"
            );
            logger.info("Created new event with ID: {}", eventId);
        }
        if (eventId == null) throw new AssertionError("No se pudo crear el evento de prueba");

        String seatsUrl = System.getProperty("admin.events.api.url",
                com.ticketing.utils.TestDataConfig.CATALOG_BASE_URL + "/admin/events")
                + "/" + eventId + "/seats";
        logger.info("Seats URL: {}", seatsUrl);
        
        String body = String.format("{\"sectionConfigurations\":[{\"sectionCode\":\"%s\",\"rows\":1,\"seatsPerRow\":1,\"priceMultiplier\":1.0}]}", sectionName);
        logger.info("Request body for seats: {}", body);
        
        try {
            java.net.http.HttpRequest.Builder reqBuilder = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(seatsUrl))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                    .timeout(java.time.Duration.ofSeconds(com.ticketing.utils.TestDataConfig.HTTP_TIMEOUT_SECONDS));
            String adminToken = com.ticketing.utils.EventApiClient.getAdminToken();
            logger.info("Admin token present: {}", adminToken != null);
            if (adminToken != null) {
                reqBuilder.header("Authorization", "Bearer " + adminToken);
            }
            java.net.http.HttpRequest request = reqBuilder.build();
            java.net.http.HttpResponse<String> response = java.net.http.HttpClient.newHttpClient().send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            logger.info("Seats API response status: {}", response.statusCode());
            logger.info("Seats API response body: {}", response.body());
        } catch (Exception e) {
            throw new AssertionError("No se pudo generar el asiento para la sección: " + sectionName, e);
        }

        logger.info("Logging in as reserve user to reserve seat");
        loginAsUser(com.ticketing.utils.TestDataConfig.RESERVE_USER_EMAIL, com.ticketing.utils.TestDataConfig.RESERVE_USER_PASSWORD);
        
        logger.info("Navigating to event details for: {}", eventName);
        navigateToEventDetails(eventName);
        
        logger.info("Clicking specific seat: {} row {} seat {}", sectionName, 1, 1);
        clickSpecificSeat(sectionName, 1, 1);
        
        logger.info("Waiting for page update after seat click");
        waitForPageUpdate();
        
        logger.info("Clicking Reserve & Add to Cart button");
        clickReserveAndAddToCart();
        
        logger.info("Waiting for page update after reservation");
        waitForPageUpdate();
        
        logger.info("Logging in as test user (User B)");
        loginAsUser(com.ticketing.utils.TestDataConfig.TEST_USER_EMAIL, com.ticketing.utils.TestDataConfig.TEST_USER_PASSWORD);
        
        logger.info("Event setup complete - section {} should now have 1 reserved seat", sectionName);
    }

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

    @Step("Getting Join Waitlist button text")
    public String getJoinWaitlistButtonText() {
        return waitlistPage.getJoinWaitlistButtonText();
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