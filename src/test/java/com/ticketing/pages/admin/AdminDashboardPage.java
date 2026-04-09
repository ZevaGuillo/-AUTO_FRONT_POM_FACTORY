package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DefaultUrl("/admin/dashboard")
public class AdminDashboardPage extends BasePage {
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(xpath = "//div[@data-slot='card' and contains(@class,'fixed')]")
    private WebElementFacade adminNavigation;
    
    @FindBy(css = "[data-events-link]")
    private WebElementFacade eventsLink;
    
    @FindBy(xpath = "//a[@href='/admin/events/create']")
    private WebElementFacade createEventLink;
    
    @FindBy(css = "[data-logout-button]")
    private WebElementFacade logoutButton;
    
    @FindBy(css = ".dashboard-stats")
    private WebElementFacade dashboardStats;
    
    @FindBy(css = "[data-total-events]")
    private WebElementFacade totalEventsCount;
    
    @FindBy(css = "[data-total-sales]")
    private WebElementFacade totalSalesAmount;
    
    @FindBy(css = "[data-active-events]")
    private WebElementFacade activeEventsCount;
    
    @FindBy(css = ".recent-orders")
    private WebElementFacade recentOrdersSection;
    
    @FindBy(css = ".order-item")
    private List<WebElementFacade> recentOrders;
    
    @FindBy(css = ".welcome-message")
    private WebElementFacade welcomeMessage;
    
    @FindBy(css = "[data-user-info]")
    private WebElementFacade userInfo;
    
    @FindBy(css = ".quick-actions")
    private WebElementFacade quickActionsSection;
    
    @FindBy(css = "[data-quick-create-event]")
    private WebElementFacade quickCreateEventButton;
    
    @FindBy(css = "[data-manage-events]")
    private WebElementFacade manageEventsButton;
    
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    public boolean isAdminNavigationDisplayed() {
        return isElementVisible(adminNavigation);
    }
    
    public void clickEventsLink() {
        waitForElementClickable(eventsLink);
        clickElement(eventsLink);
        logger.info("Navigated to Events management");
    }
    
    public void clickCreateEventLink() {
        waitForElementClickable(createEventLink);
        scrollToElement(createEventLink);
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        evaluateJavascript("arguments[0].click();", createEventLink);
        logger.info("Navigated to Create Event");
    }

    public void clickLogout() {
        waitForElementClickable(logoutButton);
        clickElement(logoutButton);
        logger.info("User logged out");
    }
    
    public boolean areDashboardStatsDisplayed() {
        return isElementVisible(dashboardStats);
    }

    public String getTotalEventsCount() {
        if (isElementVisible(totalEventsCount)) {
            return getText(totalEventsCount);
        }
        return "0";
    }

    public String getTotalSalesAmount() {
        if (isElementVisible(totalSalesAmount)) {
            return getText(totalSalesAmount);
        }
        return "$0";
    }

    public String getActiveEventsCount() {
        if (isElementVisible(activeEventsCount)) {
            return getText(activeEventsCount);
        }
        return "0";
    }

    public boolean isRecentOrdersSectionDisplayed() {
        return isElementVisible(recentOrdersSection);
    }

    public int getRecentOrdersCount() {
        return recentOrders.size();
    }

    public String getWelcomeMessage() {
        if (isElementVisible(welcomeMessage)) {
            return getText(welcomeMessage);
        }
        return "";
    }

    public String getUserInfo() {
        if (isElementVisible(userInfo)) {
            return getText(userInfo);
        }
        return "";
    }

    public boolean isQuickActionsSectionDisplayed() {
        return isElementVisible(quickActionsSection);
    }

    public void clickQuickCreateEvent() {
        if (isElementVisible(quickCreateEventButton)) {
            clickElement(quickCreateEventButton);
            logger.info("Clicked quick create event button");
        }
    }

    public void clickManageEvents() {
        if (isElementVisible(manageEventsButton)) {
            clickElement(manageEventsButton);
            logger.info("Clicked manage events button");
        }
    }

    public boolean isDashboardLoaded() {
        waitForPageLoad();
        return isElementVisible(pageTitle) && 
               isElementVisible(adminNavigation);
    }

    public boolean isUserAuthenticated() {
        try {
            waitForElement(pageTitle);
            return getCurrentUrl().contains("/admin/dashboard");
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }

    public boolean areAllDashboardSectionsPresent() {
        return isAdminNavigationDisplayed() &&
               areDashboardStatsDisplayed() &&
               isRecentOrdersSectionDisplayed() &&
               isQuickActionsSectionDisplayed();
    }

    public void navigateToSection(String section) {
        switch (section.toLowerCase()) {
            case "events":
                clickEventsLink();
                break;
            case "create-event":
                clickCreateEventLink();
                break;
            case "logout":
                clickLogout();
                break;
            default:
                logger.warn("Unknown section: {}", section);
        }
    }

    public List<String> getRecentOrdersInfo() {
        return recentOrders.stream()
                .map(this::getText)
                .toList();
    }
}
