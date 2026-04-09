package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;

import java.util.List;

import org.openqa.selenium.support.FindBy;

@DefaultUrl("/admin/events/create")
public class CreateEventPage extends BasePage {
    
    @FindBy(id = "name")
    private WebElementFacade nameInput;
    
    @FindBy(id = "description")
    private WebElementFacade descriptionInput;
    
    @FindBy(id = "eventDate")
    private WebElementFacade eventDateInput;
    
    @FindBy(id = "venue")
    private WebElementFacade venueInput;
    
    @FindBy(id = "maxCapacity")
    private WebElementFacade maxCapacityInput;
    
    @FindBy(id = "basePrice")
    private WebElementFacade basePriceInput;
    
    @FindBy(id = "imageUrl")
    private WebElementFacade imageUrlInput;

    @FindBy(id = "tags")
    private WebElementFacade tagsInput;

    @FindBy(id = "isActive")
    private WebElementFacade activeCheckbox;

    @FindBy(xpath = "//button[@type='submit' and contains(text(),'Crear Evento')]")
    private WebElementFacade submitButton;
    
    @FindBy(css = "[data-error-message]")
    private WebElementFacade errorMessage;
    
    @FindBy(css = "[data-success-message]")
    private WebElementFacade successMessage;
    
    @FindBy(xpath = "//div[@data-slot='card']")
    private WebElementFacade formContainer;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(xpath = "//a[@href='/admin/events']//button")
    private WebElementFacade cancelButton;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(xpath = "//input[@aria-invalid='true']")
    private List<WebElementFacade> fieldErrors;
    
    @FindBy(css = "[data-generate-seats-checkbox]")
    private WebElementFacade generateSeatsCheckbox;
    
    @FindBy(css = "#eventTime")
    private WebElementFacade eventTimeInput;
    
    @FindBy(css = "#category")
    private WebElementFacade categorySelect;

    @FindBy(css = ".field-error") 
    private WebElementFacade fieldError;
    
    public void fillEventName(String name) {
        waitForElement(nameInput);
        typeText(nameInput, name);
        logger.info("Filled event name: {}", name);
    }
    
    public void fillEventDescription(String description) {
        waitForElement(descriptionInput);
        typeText(descriptionInput, description);
        logger.info("Filled event description");
    }
    
    public void fillEventDate(String eventDate) {
        waitForElement(eventDateInput);

        String formattedDate = eventDate + "T10:00";

        evaluateJavascript(
            "const input = arguments[0];" +
            "const value = arguments[1];" +
            "const nativeSetter = Object.getOwnPropertyDescriptor(" +
            "window.HTMLInputElement.prototype, 'value').set;" +
            "nativeSetter.call(input, value);" +
            "input.dispatchEvent(new Event('input', { bubbles: true }));",
            eventDateInput,
            formattedDate
        );

        logger.info("Filled event date: {}", formattedDate);
    }
    
    public void fillVenue(String venue) {
        waitForElement(venueInput);
        typeText(venueInput, venue);
        logger.info("Filled venue: {}", venue);
    }
    
    public void fillMaxCapacity(String maxCapacity) {
        waitForElement(maxCapacityInput);
        typeText(maxCapacityInput, maxCapacity);
        logger.info("Filled max capacity: {}", maxCapacity);
    }
    
    public void fillBasePrice(String basePrice) {
        waitForElement(basePriceInput);
        typeText(basePriceInput, basePrice);
        logger.info("Filled base price: {}", basePrice);
    }
    
    public void fillEventTime(String eventTime) {
        if (isElementVisible(eventTimeInput)) {
            typeText(eventTimeInput, eventTime);
            logger.info("Filled event time: {}", eventTime);
        }
    }
    
    public void fillEventForm(String name, String description, String eventDate, 
                             String venue, String maxCapacity, String basePrice) {
        fillEventName(name);
        fillEventDescription(description);
        fillEventDate(eventDate);
        fillVenue(venue);
        fillMaxCapacity(maxCapacity);
        fillBasePrice(basePrice);
        logger.info("Filled complete event form");
    }
    
    public void fillEventFormWithTestData() {
        String eventName = TestUtils.generateUniqueEventName();
        String venue = TestUtils.generateRandomVenue();
        String capacity = TestUtils.generateRandomCapacity();
        String price = TestUtils.generateRandomPrice();
        String futureDate = TestUtils.generateFutureDate(30);
        
        fillEventForm(eventName, "Test event description", futureDate, 
                     venue, capacity, price);
        logger.info("Filled event form with test data");
    }
    
    public void submitForm() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Submitted event form");
    }
    
    public void clickCancel() {
        if (isElementVisible(cancelButton)) {
            clickElement(cancelButton);
            logger.info("Clicked cancel button");
        }
    }
    
    public void createEvent(String name, String description, String eventDate,
                           String venue, String maxCapacity, String basePrice) {
        fillEventForm(name, description, eventDate, venue, maxCapacity, basePrice);
        submitForm();
        logger.info("Created event: {}", name);
    }
    
    public void createEventWithTestData() {
        fillEventFormWithTestData();
        submitForm();
        logger.info("Created event with test data");
    }
    
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    public boolean isFormDisplayed() {
        return isElementVisible(formContainer);
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
    
    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }
    
    public String getSuccessMessage() {
        if (isElementVisible(successMessage)) {
            return getText(successMessage);
        }
        return "";
    }
    
    public void waitForFormProcessing() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Form processing completed");
    }
    
    public boolean isEventCreatedSuccessfully() {
        waitForFormProcessing();
        return isSuccessMessageDisplayed() || !getCurrentUrl().contains("/create");
    }
    
    public boolean isSubmitButtonEnabled() {
        waitForElement(submitButton);
        return submitButton.isEnabled();
    }
    
    public void toggleGenerateSeats() {
        if (isElementVisible(generateSeatsCheckbox)) {
            clickElement(generateSeatsCheckbox);
            logger.info("Toggled generate seats checkbox");
        }
    }
    
    public boolean isGenerateSeatsChecked() {
        if (isElementVisible(generateSeatsCheckbox)) {
            return generateSeatsCheckbox.isSelected();
        }
        return false;
    }
    
    public String getFieldError() {
        if (isElementVisible(fieldError)) {
            return getText(fieldError);
        }
        return "";
    }
    
    public void clearAllFields() {
        if (isElementVisible(nameInput)) nameInput.clear();
        if (isElementVisible(descriptionInput)) descriptionInput.clear();
        if (isElementVisible(eventDateInput)) eventDateInput.clear();
        if (isElementVisible(venueInput)) venueInput.clear();
        if (isElementVisible(maxCapacityInput)) maxCapacityInput.clear();
        if (isElementVisible(basePriceInput)) basePriceInput.clear();
        logger.info("Cleared all form fields");
    }
    
    public boolean areAllRequiredFieldsPresent() {
        return isElementVisible(nameInput) &&
               isElementVisible(descriptionInput) &&
               isElementVisible(eventDateInput) &&
               isElementVisible(venueInput) &&
               isElementVisible(maxCapacityInput) &&
               isElementVisible(basePriceInput) &&
               isElementVisible(submitButton);
    }

    public String getCurrentEventName() {
        waitForElement(nameInput);
        return nameInput.getValue();
    }

    public String getCurrentEventDescription() {
        waitForElement(descriptionInput);
        return descriptionInput.getValue();
    }

    public WebElementFacade getEventDateInput() {
        return eventDateInput;
    }

    public boolean isDatePickerVisible() {
        return isElementVisible(eventDateInput);
    }
}
