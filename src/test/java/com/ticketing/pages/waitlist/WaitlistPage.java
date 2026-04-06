package com.ticketing.pages.waitlist;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Waitlist-specific UI components on the Event Details page.
 * Handles: Join Waitlist button, waitlist banner, duplicate error message,
 * and toast notifications.
 */
public class WaitlistPage extends BasePage {

    // "Join Waitlist" button (amber-styled, inside the seat info panel)
    private static final String JOIN_WAITLIST_XPATH =
            "//button[contains(text(), 'Join Waitlist')]";

    // Waitlist banner title: "You're on the waitlist"
    private static final String BANNER_TITLE_XPATH =
            "//span[contains(@class,'font-medium') and contains(text(),\"You're on the waitlist\")]";

    // Badge inside waitlist banner: "Section General: Position #2"
    private static final String BANNER_BADGE_XPATH =
            "//span[@data-slot='badge' and contains(text(),'Position')]";

    // Duplicate error: "User is already in waitlist for this event and section"
    @FindBy(css = "div.border-destructive\\/30.bg-destructive\\/10")
    private WebElementFacade duplicateErrorMessage;

    // Seat reserved warning text in seat info panel
    @FindBy(css = "p.text-xs.text-amber-600")
    private WebElementFacade seatReservedWarning;

    // Toast notification (generic — Sonner or similar)
    @FindBy(css = "[data-sonner-toast], [role='status'], .toast-notification")
    private WebElementFacade toastNotification;

    // ========================================================================
    // Join Waitlist
    // ========================================================================

    public boolean isJoinWaitlistButtonVisible() {
        try {
            WebElement btn = getDriver().findElement(By.xpath(JOIN_WAITLIST_XPATH));
            return btn.isDisplayed();
        } catch (Exception e) {
            logger.debug("Join Waitlist button not visible");
            return false;
        }
    }

    public void clickJoinWaitlistButton() {
        logger.info("Clicking Join Waitlist button");
        WebElement btn = waitForElement(By.xpath(JOIN_WAITLIST_XPATH));
        btn.click();
    }

    // ========================================================================
    // Waitlist Banner (visible when already subscribed)
    // ========================================================================

    public boolean isWaitlistBannerVisible() {
        try {
            WebElement title = waitForElement(By.xpath(BANNER_TITLE_XPATH));
            return title.isDisplayed();
        } catch (Exception e) {
            logger.debug("Waitlist banner not visible");
            return false;
        }
    }

    public String getWaitlistBannerTitle() {
        try {
            WebElement title = getDriver().findElement(By.xpath(BANNER_TITLE_XPATH));
            return title.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getWaitlistBadgeText() {
        try {
            WebElement badge = getDriver().findElement(By.xpath(BANNER_BADGE_XPATH));
            return badge.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ========================================================================
    // Duplicate error message
    // ========================================================================

    public boolean isDuplicateErrorVisible() {
        try {
            return isElementVisible(duplicateErrorMessage);
        } catch (Exception e) {
            return false;
        }
    }

    public String getDuplicateErrorText() {
        return getText(duplicateErrorMessage);
    }

    // ========================================================================
    // Toast notifications
    // ========================================================================

    public boolean isToastVisible() {
        try {
            return waitForElement(toastNotification).isDisplayed();
        } catch (Exception e) {
            logger.debug("Toast notification not visible");
            return false;
        }
    }

    public String getToastMessage() {
        return getText(toastNotification);
    }

    public void waitForToastToDismiss() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for toast to dismiss", e);
            Thread.currentThread().interrupt();
        }
    }
}
