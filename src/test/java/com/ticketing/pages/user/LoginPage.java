package com.ticketing.pages.user;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

@DefaultUrl("/login")
public class LoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElementFacade emailInput;

    @FindBy(id = "password")
    private WebElementFacade passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade submitButton;

    @FindBy(css = "[data-slot='card-title']")
    private WebElementFacade pageTitle;

    public void typeEmail(String email) {
        waitForElement(emailInput);
        typeText(emailInput, email);
    }

    public void typePassword(String password) {
        waitForElement(passwordInput);
        typeText(passwordInput, password);
    }

    public void clickSubmit() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
    }

    public void loginAs(String email, String password) {
        typeEmail(email);
        typePassword(password);
        clickSubmit();
    }

    public boolean isLoginPageDisplayed() {
        try {
            return waitForElement(pageTitle).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }
}
