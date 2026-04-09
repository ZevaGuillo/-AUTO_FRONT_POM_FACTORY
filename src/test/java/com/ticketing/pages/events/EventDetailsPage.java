package com.ticketing.pages.events;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EventDetailsPage extends BasePage {

    @FindBy(css = "h1.text-3xl")
    private WebElementFacade eventTitle;

    @FindBy(css = "a[href='/']")
    private WebElementFacade backToEventsLink;

    private static final String SECTION_HEADER_CSS = "h3.text-sm.font-semibold.text-accent";

    private static final String ALL_SEATS_CSS = "button[aria-label^='Seat']";

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30")
    private WebElementFacade seatInfoPanel;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-sm.font-medium")
    private WebElementFacade seatInfoText;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-lg")
    private WebElementFacade seatPriceText;

    @FindBy(css = "div.rounded-lg.border.border-accent\\/30 p.text-xs.text-amber-600")
    private WebElementFacade seatReservedWarning;

    public boolean isEventDetailsPageLoaded() {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(
                    getDriver(), java.time.Duration.ofSeconds(15))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions
                            .urlMatches(".*/events/[0-9a-fA-F-]+$"));

            waitForElement(eventTitle);

            String titleText = eventTitle.getText().trim();
            if ("Events".equalsIgnoreCase(titleText) || titleText.isEmpty()) {
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

    public List<WebElement> getSectionHeaders() {
        return getDriver().findElements(By.cssSelector(SECTION_HEADER_CSS));
    }

    public boolean isSectionVisible(String sectionName) {
        for (WebElement header : getSectionHeaders()) {
            if (header.getText().toUpperCase().contains(sectionName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public WebElement findSeatByAriaLabel(String ariaLabelContains) {
        String xpath = String.format("//button[contains(@aria-label, '%s')]", ariaLabelContains);
        return waitForElement(By.xpath(xpath));
    }

    public void clickSeat(String section, int row, int seat) {
        String ariaFragment = String.format("%s%d-%d", section, row, seat);
        logger.info("Clicking seat with aria-label containing: {}", ariaFragment);
        WebElement seatButton = findSeatByAriaLabel(ariaFragment);
        ((org.openqa.selenium.JavascriptExecutor) getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", seatButton);
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        ((org.openqa.selenium.JavascriptExecutor) getDriver())
                .executeScript("arguments[0].click();", seatButton);
    }

    public List<WebElement> findSeatsByStatus(String section, String status) {
        String xpath = String.format("//button[contains(@aria-label, '%s') and contains(@aria-label, '%s')]",
                section, status);
        return getDriver().findElements(By.xpath(xpath));
    }

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

    public void clickFirstSeatInSection(String section) {
        logger.info("Clicking first seat in section: {}", section);
        String xpath = String.format("//button[contains(@aria-label, 'Seat %s')]", section);
        List<WebElement> seats = getDriver().findElements(By.xpath(xpath));
        if (seats.isEmpty()) {
            throw new AssertionError("No seats found in section: " + section);
        }
        seats.get(0).click();
    }

    public boolean isSeatInfoPanelVisible() {
        try {
            return waitForElement(seatInfoPanel).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSeatInfoText() {
        return getText(seatInfoText);
    }

    public String getSeatPrice() {
        return getText(seatPriceText);
    }

    public boolean isReservedWarningVisible() {
        try {
            return isElementVisible(seatReservedWarning);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sectionHasAvailableSeats(String section) {
        return !findSeatsByStatus(section, "Available").isEmpty();
    }

    public boolean sectionHasReservedSeats(String section) {
        return !findSeatsByStatus(section, "Reserved").isEmpty();
    }

    private static final String RESERVE_BUTTON_XPATH =
            "//button[contains(text(), 'Reserve') and contains(text(), 'Add to Cart')]";

    public void clickReserveAndAddToCart() {
        logger.info("Clicking 'Reserve & Add to Cart' button");
        WebElement btn = waitForElement(By.xpath(RESERVE_BUTTON_XPATH));
        btn.click();
    }

    public boolean isReserveButtonVisible() {
        try {
            return !getDriver().findElements(By.xpath(RESERVE_BUTTON_XPATH)).isEmpty()
                    && getDriver().findElement(By.xpath(RESERVE_BUTTON_XPATH)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
