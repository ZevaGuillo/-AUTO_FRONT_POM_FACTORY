package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

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
    
    @FindBy(xpath = "//div[@data-slot='card-content']")
    private WebElementFacade loginForm;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(css = ".forgot-password")
    private WebElementFacade forgotPasswordLink;
    
    @FindBy(css = "[data-remember-me]")
    private WebElementFacade rememberMeCheckbox;
    
    public void typeEmail(String email) {
        waitForElement(emailInput);
        typeText(emailInput, email);
        logger.info("Typed email: {}", email);
    }
    
    public void typePassword(String password) {
        waitForElement(passwordInput);
        typeText(passwordInput, password);
        logger.info("Typed password");
    }
    
    public void clickSubmit() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Clicked submit button");
    }
    
    public void login(String email, String password) {
        typeEmail(email);
        typePassword(password);
        clickSubmit();
        logger.info("Performed login with email: {}", email);
    }
    
    public void loginWithDefaultCredentials() {
        String adminEmail = TestUtils.getAdminEmail();
        String adminPassword = TestUtils.getAdminPassword();
        login(adminEmail, adminPassword);
        logger.info("Logged in with default admin credentials");
    }
    
    public void clickAutoFill() {
        if (isElementVisible(autoFillButton)) {
            clickElement(autoFillButton);
            logger.info("Clicked auto-fill button");
        }
    }
    
    public void togglePasswordVisibility() {
        if (isElementVisible(showPasswordToggle)) {
            clickElement(showPasswordToggle);
            logger.info("Toggled password visibility");
        }
    }
    
    public boolean isLoginFormDisplayed() {
        return isElementVisible(loginForm);
    }
    
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }
    
    public String getErrorMessage() {
        if (isElementVisible(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    public boolean isSubmitButtonEnabled() {
        waitForElement(submitButton);
        return submitButton.isEnabled();
    }
    
    public void waitForLoginProcessing() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Login processing completed");
    }
    
    public boolean isLoginSuccessful() {
        waitForLoginProcessing();
        return !getCurrentUrl().contains("/admin/login");
    }
    
    public void clickForgotPassword() {
        if (isElementVisible(forgotPasswordLink)) {
            clickElement(forgotPasswordLink);
            logger.info("Clicked forgot password link");
        }
    }
    
    public void toggleRememberMe() {
        if (isElementVisible(rememberMeCheckbox)) {
            clickElement(rememberMeCheckbox);
            logger.info("Toggled remember me checkbox");
        }
    }
    
    public boolean isRememberMeChecked() {
        if (isElementVisible(rememberMeCheckbox)) {
            return rememberMeCheckbox.isSelected();
        }
        return false;
    }
    
    public String getEmailValue() {
        waitForElement(emailInput);
        return emailInput.getValue();
    }
    
    public String getPasswordValue() {
        waitForElement(passwordInput);
        return passwordInput.getValue();
    }
    
    public void clearFields() {
        waitForElement(emailInput);
        waitForElement(passwordInput);
        emailInput.clear();
        passwordInput.clear();
        logger.info("Cleared email and password fields");
    }
    
    public boolean areAllFormElementsPresent() {
        return isElementVisible(emailInput) &&
               isElementVisible(passwordInput) &&
               isElementVisible(submitButton) &&
               isElementVisible(loginForm);
    }
    
    public String loginWithInvalidCredentials(String email, String password) {
        login(email, password);
        waitForLoginProcessing();
        return getErrorMessage();
    }
    
    public void enterEmail(String email) {
        waitForElement(emailInput);
        emailInput.clear();
        emailInput.type(email);
        logger.info("Entered email: " + email);
    }
    
    public void enterPassword(String password) {
        waitForElement(passwordInput);
        passwordInput.clear();
        passwordInput.type(password);
        logger.info("Entered password");
    }
    
    public void clickLoginButton() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Clicked login button");
    }

    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }
}
