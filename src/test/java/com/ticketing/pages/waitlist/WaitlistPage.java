package com.ticketing.pages.waitlist;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WaitlistPage extends BasePage {

    private static final String JOIN_WAITLIST_XPATH =
            "//button[contains(text(), 'Join Waitlist')]";

    private static final String BANNER_TITLE_XPATH =
            "//span[contains(@class,'font-medium') and contains(text(),\"You're on the waitlist\")]";

    private static final String BANNER_BADGE_XPATH =
            "//span[@data-slot='badge' and contains(text(),'Position')]";

    private static final String DUPLICATE_ERROR_XPATH =
            "//div[contains(@class,'border-destructive') and contains(@class,'bg-destructive') and contains(text(),'already in waitlist')]";

    @FindBy(css = "p.text-xs.text-amber-600")
    private WebElementFacade seatReservedWarning;

    @FindBy(css = "[data-sonner-toast], [role='status'], .toast-notification")
    private WebElementFacade toastNotification;


    private static final String WAITLIST_BUTTON_XPATH =
            "//button[contains(text(), 'Join Waitlist') or contains(text(), 'On Waitlist')]";

    public boolean isJoinWaitlistButtonVisible() {
        try {
            WebElement btn = getDriver().findElement(By.xpath(WAITLIST_BUTTON_XPATH));
            return btn.isDisplayed();
        } catch (Exception e) {
            logger.debug("Join Waitlist button not visible");
            return false;
        }
    }

    public void clickJoinWaitlistButton() {
        logger.info("Clicking Join Waitlist button");
        WebElement btn = waitForElement(By.xpath(WAITLIST_BUTTON_XPATH));
        btn.click();
    }

    public String getJoinWaitlistButtonText() {
        try {
            WebElement btn = getDriver().findElement(By.xpath(WAITLIST_BUTTON_XPATH));
            return btn.getText();
        } catch (Exception e) {
            logger.debug("Could not get button text");
            return "";
        }
    }

    public boolean isOnWaitlistButtonVisible() {
        try {
            WebElement btn = getDriver().findElement(By.xpath("//button[contains(text(), 'On Waitlist')]"));
            return btn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


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

    public boolean isDuplicateErrorVisible() {
        try {
            WebElement el = getDriver().findElement(By.xpath(DUPLICATE_ERROR_XPATH));
            return el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDuplicateErrorText() {
        try {
            return getDriver().findElement(By.xpath(DUPLICATE_ERROR_XPATH)).getText();
        } catch (Exception e) {
            return "";
        }
    }

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
