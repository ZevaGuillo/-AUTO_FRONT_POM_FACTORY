package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Admin Dashboard page
 * URL: /admin/dashboard
 */
@DefaultUrl("/admin/dashboard")
public class AdminDashboardPage extends BasePage {
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".admin-nav")
    private WebElementFacade adminNavigation;
    
    @FindBy(css = "[data-events-link]")
    private WebElementFacade eventsLink;
    
    @FindBy(css = "[data-create-event-link]")
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
    
    /**
     * Get the page title
     * @return Page title text
     */
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    /**
     * Check if admin navigation is displayed
     * @return true if admin navigation is visible
     */
    public boolean isAdminNavigationDisplayed() {
        return isElementVisible(adminNavigation);
    }
    
    /**
     * Click on Events management link
     */
    public void clickEventsLink() {
        waitForElementClickable(eventsLink);
        clickElement(eventsLink);
        logger.info("Navigated to Events management");
    }
    
    /**
     * Click on Create Event link
     */
    public void clickCreateEventLink() {
        waitForElementClickable(createEventLink);
        clickElement(createEventLink);
        logger.info("Navigated to Create Event");
    }
    
    /**
     * Click logout button
     */
    public void clickLogout() {
        waitForElementClickable(logoutButton);
        clickElement(logoutButton);
        logger.info("User logged out");
    }
    
    /**
     * Check if dashboard statistics are displayed
     * @return true if stats section is visible
     */
    public boolean areDashboardStatsDisplayed() {
        return isElementVisible(dashboardStats);
    }
    
    /**
     * Get total events count
     * @return Total events count text
     */
    public String getTotalEventsCount() {
        if (isElementVisible(totalEventsCount)) {
            return getText(totalEventsCount);
        }
        return "0";
    }
    
    /**
     * Get total sales amount
     * @return Total sales amount text
     */
    public String getTotalSalesAmount() {
        if (isElementVisible(totalSalesAmount)) {
            return getText(totalSalesAmount);
        }
        return "$0";
    }
    
    /**
     * Get active events count
     * @return Active events count text
     */
    public String getActiveEventsCount() {
        if (isElementVisible(activeEventsCount)) {
            return getText(activeEventsCount);
        }
        return "0";
    }
    
    /**
     * Check if recent orders section is displayed
     * @return true if recent orders section is visible
     */
    public boolean isRecentOrdersSectionDisplayed() {
        return isElementVisible(recentOrdersSection);
    }
    
    /**
     * Get count of recent orders displayed
     * @return Number of recent orders
     */
    public int getRecentOrdersCount() {
        return recentOrders.size();
    }
    
    /**
     * Get welcome message text
     * @return Welcome message text
     */
    public String getWelcomeMessage() {
        if (isElementVisible(welcomeMessage)) {
            return getText(welcomeMessage);
        }
        return "";
    }
    
    /**
     * Get user info text
     * @return User info text
     */
    public String getUserInfo() {
        if (isElementVisible(userInfo)) {
            return getText(userInfo);
        }
        return "";
    }
    
    /**
     * Check if quick actions section is displayed
     * @return true if quick actions are visible
     */
    public boolean isQuickActionsSectionDisplayed() {
        return isElementVisible(quickActionsSection);
    }
    
    /**
     * Click quick create event button
     */
    public void clickQuickCreateEvent() {
        if (isElementVisible(quickCreateEventButton)) {
            clickElement(quickCreateEventButton);
            logger.info("Clicked quick create event button");
        }
    }
    
    /**
     * Click manage events button
     */
    public void clickManageEvents() {
        if (isElementVisible(manageEventsButton)) {
            clickElement(manageEventsButton);
            logger.info("Clicked manage events button");
        }
    }
    
    /**
     * Verify that dashboard has loaded completely
     * @return true if all main dashboard elements are visible
     */
    public boolean isDashboardLoaded() {
        waitForPageLoad();
        return isElementVisible(pageTitle) && 
               isElementVisible(adminNavigation) &&
               isElementVisible(dashboardStats);
    }
    
    /**
     * Check if user is authenticated (dashboard is accessible)
     * @return true if dashboard is accessible
     */
    public boolean isUserAuthenticated() {
        try {
            waitForElement(pageTitle);
            return getCurrentUrl().contains("/admin/dashboard");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get current URL to verify navigation
     * @return Current page URL
     */
    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }
    
    /**
     * Verify all dashboard sections are present
     * @return true if all main sections are displayed
     */
    public boolean areAllDashboardSectionsPresent() {
        return isAdminNavigationDisplayed() &&
               areDashboardStatsDisplayed() &&
               isRecentOrdersSectionDisplayed() &&
               isQuickActionsSectionDisplayed();
    }
    
    /**
     * Navigate to a specific admin section
     * @param section The section to navigate to ("events", "create-event", "logout")
     */
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
    
    /**
     * Get all recent order information as text
     * @return List of recent order texts
     */
    public List<String> getRecentOrdersInfo() {
        return recentOrders.stream()
                .map(this::getText)
                .toList();
    }
}