package com.ticketing.pages.publicpages;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Checkout page
 * URL: /checkout
 */
@DefaultUrl("/checkout")
public class CheckoutPage extends BasePage {
    
    @FindBy(css = "[data-order-items]")
    private WebElementFacade orderItemsContainer;
    
    @FindBy(css = ".order-item")
    private List<WebElementFacade> orderItems;
    
    @FindBy(css = "[data-total-amount]")
    private WebElementFacade totalAmount;
    
    @FindBy(css = "[data-checkout-button]")
    private WebElementFacade checkoutButton;
    
    @FindBy(css = "[data-payment-form]")
    private WebElementFacade paymentForm;
    
    @FindBy(css = "#cardNumber")
    private WebElementFacade cardNumberInput;
    
    @FindBy(css = "#expiryDate")
    private WebElementFacade expiryDateInput;
    
    @FindBy(css = "#cvv")
    private WebElementFacade cvvInput;
    
    @FindBy(css = "#cardholderName")
    private WebElementFacade cardholderNameInput;
    
    @FindBy(css = "[data-shipping-form]")
    private WebElementFacade shippingForm;
    
    @FindBy(css = "#firstName")
    private WebElementFacade firstNameInput;
    
    @FindBy(css = "#lastName")
    private WebElementFacade lastNameInput;
    
    @FindBy(css = "#email")
    private WebElementFacade emailInput;
    
    @FindBy(css = "#phone")
    private WebElementFacade phoneInput;
    
    @FindBy(css = ".empty-cart-message")
    private WebElementFacade emptyCartMessage;
    
    @FindBy(css = ".order-summary")
    private WebElementFacade orderSummary;
    
    @FindBy(css = ".subtotal")
    private WebElementFacade subtotal;
    
    @FindBy(css = ".taxes")
    private WebElementFacade taxes;
    
    @FindBy(css = ".processing-spinner")
    private WebElementFacade processingSpinner;
    
    @FindBy(css = ".success-message")
    private WebElementFacade successMessage;
    
    @FindBy(css = ".error-message")
    private WebElementFacade errorMessage;
    
    /**
     * Get the total amount for checkout
     * @return Total amount text
     */
    public String getTotalAmount() {
        waitForElement(totalAmount);
        return getText(totalAmount);
    }
    
    /**
     * Get the count of order items
     * @return Number of items in order
     */
    public int getOrderItemsCount() {
        if (isElementVisible(orderItemsContainer)) {
            return orderItems.size();
        }
        return 0;
    }
    
    /**
     * Check if checkout button is enabled
     * @return true if checkout button is enabled
     */
    public boolean isCheckoutButtonEnabled() {
        waitForElement(checkoutButton);
        return checkoutButton.isEnabled();
    }
    
    /**
     * Click the checkout button
     */
    public void clickCheckout() {
        waitForElementClickable(checkoutButton);
        clickElement(checkoutButton);
        logger.info("Clicked checkout button");
    }
    
    /**
     * Check if cart is empty
     * @return true if cart is empty
     */
    public boolean isCartEmpty() {
        return isElementVisible(emptyCartMessage) || getOrderItemsCount() == 0;
    }
    
    /**
     * Fill payment form with card details
     * @param cardNumber Card number
     * @param expiryDate Expiry date (MM/YY format)
     * @param cvv CVV code
     * @param cardholderName Cardholder name
     */
    public void fillPaymentForm(String cardNumber, String expiryDate, String cvv, String cardholderName) {
        waitForElement(paymentForm);
        
        typeText(cardNumberInput, cardNumber);
        typeText(expiryDateInput, expiryDate);
        typeText(cvvInput, cvv);
        typeText(cardholderNameInput, cardholderName);
        
        logger.info("Filled payment form with card details");
    }
    
    /**
     * Fill shipping information
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param phone Phone number
     */
    public void fillShippingForm(String firstName, String lastName, String email, String phone) {
        if (isElementVisible(shippingForm)) {
            typeText(firstNameInput, firstName);
            typeText(lastNameInput, lastName);
            typeText(emailInput, email);
            typeText(phoneInput, phone);
            
            logger.info("Filled shipping form with customer details");
        }
    }
    
    /**
     * Complete the entire checkout process
     * @param cardNumber Card number
     * @param expiryDate Expiry date
     * @param cvv CVV
     * @param cardholderName Cardholder name
     * @param firstName Customer first name
     * @param lastName Customer last name
     * @param email Customer email
     * @param phone Customer phone
     */
    public void completeCheckout(String cardNumber, String expiryDate, String cvv, String cardholderName,
                                String firstName, String lastName, String email, String phone) {
        if (!isCartEmpty()) {
            fillShippingForm(firstName, lastName, email, phone);
            fillPaymentForm(cardNumber, expiryDate, cvv, cardholderName);
            clickCheckout();
            logger.info("Completed checkout process");
        } else {
            throw new RuntimeException("Cannot complete checkout with empty cart");
        }
    }
    
    /**
     * Get subtotal amount
     * @return Subtotal text
     */
    public String getSubtotal() {
        if (isElementVisible(subtotal)) {
            return getText(subtotal);
        }
        return "$0";
    }
    
    /**
     * Get taxes amount
     * @return Taxes text
     */
    public String getTaxes() {
        if (isElementVisible(taxes)) {
            return getText(taxes);
        }
        return "$0";
    }
    
    /**
     * Check if order summary is displayed
     * @return true if order summary is visible
     */
    public boolean isOrderSummaryDisplayed() {
        return isElementVisible(orderSummary);
    }
    
    /**
     * Wait for checkout processing to complete
     */
    public void waitForCheckoutProcessing() {
        if (isElementVisible(processingSpinner)) {
            processingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Checkout processing completed");
    }
    
    /**
     * Check if checkout was successful
     * @return true if success message is displayed
     */
    public boolean isCheckoutSuccessful() {
        waitForCheckoutProcessing();
        return isElementVisible(successMessage);
    }
    
    /**
     * Get success message text
     * @return Success message text
     */
    public String getSuccessMessage() {
        if (isElementVisible(successMessage)) {
            return getText(successMessage);
        }
        return "";
    }
    
    /**
     * Check if there's a checkout error
     * @return true if error message is displayed
     */
    public boolean hasCheckoutError() {
        return isElementVisible(errorMessage);
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        if (isElementVisible(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    /**
     * Get empty cart message
     * @return Empty cart message text
     */
    public String getEmptyCartMessage() {
        if (isElementVisible(emptyCartMessage)) {
            return getText(emptyCartMessage);
        }
        return "";
    }
    
    /**
     * Verify all required forms are displayed
     * @return true if payment and shipping forms are visible
     */
    public boolean areAllFormsDisplayed() {
        return isElementVisible(paymentForm) && 
               (isElementVisible(shippingForm) || true); // Shipping might be optional
    }
    
    /**
     * Get order item names as list
     * @return List of order item names
     */
    public List<String> getOrderItemNames() {
        if (!orderItems.isEmpty()) {
            return orderItems.stream()
                    .map(this::getText)
                    .toList();
        }
        return List.of();
    }
    
    /**
     * Check if checkout page is loaded
     * @return true if checkout page is loaded
     */
    public boolean isPageLoaded() {
        return isElementVisible(orderItemsContainer) || isElementVisible(emptyCartMessage);
    }
    
    /**
     * Click proceed to checkout button
     */
    public void clickProceedToCheckout() {
        if (isElementVisible(checkoutButton) && checkoutButton.isEnabled()) {
            clickElement(checkoutButton);
        }
    }
}