package com.ticketing.steps;

import com.ticketing.pages.admin.AdminDashboardPage;
import com.ticketing.pages.admin.AdminLoginPage;
import com.ticketing.pages.admin.CreateEventPage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.annotations.Step;

/**
 * Steps class for Admin related actions
 * Acts as orchestrator between StepDefinitions and PageObjects
 */
public class AdminSteps {
    
    private AdminLoginPage adminLoginPage;
    private AdminDashboardPage adminDashboardPage;
    private CreateEventPage createEventPage;
    
    @Step("Navigate to admin login page")
    public void navigateToAdminLogin() {
        adminLoginPage.open();
        TestUtils.logTestStep("Navigated to admin login page");
    }
    
    @Step("Login as admin with default credentials")
    public void loginAsAdminWithDefaults() {
        adminLoginPage.loginWithDefaultCredentials();
        adminLoginPage.waitForLoginProcessing();
        TestUtils.logTestStep("Logged in as admin with default credentials");
    }
    
    @Step("Verify login form is displayed")
    public boolean verifyLoginFormDisplayed() {
        boolean displayed = adminLoginPage.isLoginFormDisplayed();
        TestUtils.logTestStep("Verified login form displayed: " + displayed);
        return displayed;
    }
    
    @Step("Verify login was successful")
    public boolean verifyLoginSuccessful() {
        boolean successful = adminLoginPage.isLoginSuccessful();
        TestUtils.logTestStep("Verified login successful: " + successful);
        return successful;
    }
    
    @Step("Navigate to admin dashboard")
    public void navigateToAdminDashboard() {
        adminDashboardPage.open();
        TestUtils.logTestStep("Navigated to admin dashboard");
    }
    
    @Step("Verify dashboard is loaded")
    public boolean verifyDashboardLoaded() {
        boolean loaded = adminDashboardPage.isDashboardLoaded();
        TestUtils.logTestStep("Verified dashboard loaded: " + loaded);
        return loaded;
    }
    
    @Step("Navigate to create event")
    public void navigateToCreateEvent() {
        adminDashboardPage.clickCreateEventLink();
        TestUtils.logTestStep("Navigated to create event");
    }
    
    @Step("Create new event with details")
    public void createNewEvent(String name, String description, String eventDate,
                              String venue, String maxCapacity, String basePrice) {
        createEventPage.createEvent(name, description, eventDate, venue, maxCapacity, basePrice);
        TestUtils.logTestStep("Created new event: " + name);
    }
    
    @Step("Verify event creation was successful")
    public boolean verifyEventCreationSuccessful() {
        boolean successful = createEventPage.isEventCreatedSuccessfully();
        TestUtils.logTestStep("Verified event creation successful: " + successful);
        return successful;
    }
    
    @Step("Complete admin login and navigate to dashboard")
    public void completeAdminLoginAndNavigateToDashboard() {
        navigateToAdminLogin();
        loginAsAdminWithDefaults();
        if (verifyLoginSuccessful()) {
            TestUtils.logTestStep("Admin login completed successfully, user is on dashboard");
        } else {
            throw new RuntimeException("Admin login failed");
        }
    }
}