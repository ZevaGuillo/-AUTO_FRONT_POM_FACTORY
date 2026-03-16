package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the Admin Login page
 * URL: /admin/login
 */
@DefaultUrl("/admin/login")
public class AdminLoginPage extends BasePage {
    
    @FindBy(id = "email")
    private WebElementFacade emailInput;
    
    @FindBy(id = "password")
    private WebElementFacade passwordInput;
    
    @FindBy(css = "button[type='submit']")
    private WebElementFacade submitButton;
    
    @FindBy(css = "[data-show-password]")
    private WebElementFacade showPasswordToggle;
    
    @FindBy(css = "[data-error-message]")
    private WebElementFacade errorMessage;
    
    @FindBy(css = "[data-auto-fill]")
    private WebElementFacade autoFillButton;
    
    @FindBy(css = ".login-form")
    private WebElementFacade loginForm;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(css = ".forgot-password")
    private WebElementFacade forgotPasswordLink;
    
    @FindBy(css = "[data-remember-me]")
    private WebElementFacade rememberMeCheckbox;
    
    /**
     * Type email in the email input field
     * @param email Email to type
     */
    public void typeEmail(String email) {
        waitForElement(emailInput);
        typeText(emailInput, email);
        logger.info("Typed email: {}", email);
    }
    
    /**
     * Type password in the password input field
     * @param password Password to type
     */
    public void typePassword(String password) {
        waitForElement(passwordInput);
        typeText(passwordInput, password);
        logger.info("Typed password");
    }
    
    /**
     * Click the submit/login button
     */
    public void clickSubmit() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Clicked submit button");
    }
    
    /**
     * Perform login with email and password
     * @param email Admin email
     * @param password Admin password
     */
    public void login(String email, String password) {
        typeEmail(email);
        typePassword(password);
        clickSubmit();
        logger.info("Performed login with email: {}", email);
    }
    
    /**
     * Perform login with default admin credentials from config
     */
    public void loginWithDefaultCredentials() {
        String adminEmail = TestUtils.getAdminEmail();
        String adminPassword = TestUtils.getAdminPassword();
        login(adminEmail, adminPassword);
        logger.info("Logged in with default admin credentials");
    }
    
    /**
     * Click auto-fill button to populate credentials automatically
     */
    public void clickAutoFill() {
        if (isElementVisible(autoFillButton)) {
            clickElement(autoFillButton);
            logger.info("Clicked auto-fill button");
        }
    }
    
    /**
     * Toggle password visibility
     */
    public void togglePasswordVisibility() {
        if (isElementVisible(showPasswordToggle)) {
            clickElement(showPasswordToggle);
            logger.info("Toggled password visibility");
        }
    }
    
    /**
     * Check if login form is displayed
     * @return true if login form is visible
     */
    public boolean isLoginFormDisplayed() {
        return isElementVisible(loginForm);
    }
    
    /**
     * Get the page title
     * @return Page title text
     */
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }
    
    /**
     * Get the error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        if (isElementVisible(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    /**
     * Check if submit button is enabled
     * @return true if submit button is enabled
     */
    public boolean isSubmitButtonEnabled() {
        waitForElement(submitButton);
        return submitButton.isEnabled();
    }
    
    /**
     * Wait for login processing to complete
     */
    public void waitForLoginProcessing() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Login processing completed");
    }
    
    /**
     * Check if login was successful (by checking if we're redirected away from login page)
     * @return true if no longer on login page
     */
    public boolean isLoginSuccessful() {
        waitForLoginProcessing();
        return !getCurrentUrl().contains("/admin/login");
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        if (isElementVisible(forgotPasswordLink)) {
            clickElement(forgotPasswordLink);
            logger.info("Clicked forgot password link");
        }
    }
    
    /**
     * Toggle remember me checkbox
     */
    public void toggleRememberMe() {
        if (isElementVisible(rememberMeCheckbox)) {
            clickElement(rememberMeCheckbox);
            logger.info("Toggled remember me checkbox");
        }
    }
    
    /**
     * Check if remember me checkbox is checked
     * @return true if checkbox is selected
     */
    public boolean isRememberMeChecked() {
        if (isElementVisible(rememberMeCheckbox)) {
            return rememberMeCheckbox.isSelected();
        }
        return false;
    }
    
    /**
     * Get current email input value
     * @return Email input value
     */
    public String getEmailValue() {
        waitForElement(emailInput);
        return emailInput.getValue();
    }
    
    /**
     * Get current password input value
     * @return Password input value
     */
    public String getPasswordValue() {
        waitForElement(passwordInput);
        return passwordInput.getValue();
    }
    
    /**
     * Clear both email and password fields
     */
    public void clearFields() {
        waitForElement(emailInput);
        waitForElement(passwordInput);
        emailInput.clear();
        passwordInput.clear();
        logger.info("Cleared email and password fields");
    }
    
    /**
     * Verify all login form elements are present
     * @return true if all required elements are visible
     */
    public boolean areAllFormElementsPresent() {
        return isElementVisible(emailInput) &&
               isElementVisible(passwordInput) &&
               isElementVisible(submitButton) &&
               isElementVisible(loginForm);
    }
    
    /**
     * Attempt login with invalid credentials and wait for error
     * @param email Invalid email
     * @param password Invalid password
     * @return Error message text
     */
    public String loginWithInvalidCredentials(String email, String password) {
        login(email, password);
        waitForLoginProcessing();
        return getErrorMessage();
    }
    
    /**
     * Enter email into email input field
     * @param email Email to enter
     */
    public void enterEmail(String email) {
        waitForElement(emailInput);
        emailInput.clear();
        emailInput.type(email);
        logger.info("Entered email: " + email);
    }
    
    /**
     * Enter password into password input field
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        waitForElement(passwordInput);
        passwordInput.clear();
        passwordInput.type(password);
        logger.info("Entered password");
    }
    
    /**
     * Click the login button to submit credentials
     */
    public void clickLoginButton() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Clicked login button");
    }

    /**
     * Get current page URL
     * @return Current page URL
     */
    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }
}