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

    // Waitlist banner — amber box with "You're on the waitlist" text
    @FindBy(css = "div.border-amber-200.bg-amber-50")
    private WebElementFacade waitlistBanner;

    // Banner title text: "You're on the waitlist"
    @FindBy(css = "div.border-amber-200 span.font-medium")
    private WebElementFacade waitlistBannerTitle;

    // Badge inside waitlist banner: "Section General: Position #1"
    @FindBy(css = "div.border-amber-200 span[data-slot='badge']")
    private WebElementFacade waitlistBadge;

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
            return isElementVisible(waitlistBanner);
        } catch (Exception e) {
            logger.debug("Waitlist banner not visible");
            return false;
        }
    }

    public String getWaitlistBannerTitle() {
        return getText(waitlistBannerTitle);
    }

    public String getWaitlistBadgeText() {
        return getText(waitlistBadge);
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
