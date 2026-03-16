package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import com.ticketing.utils.TestUtils;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the Create Event page
 * URL: /admin/events/create
 */
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
    
    @FindBy(css = "button[type='submit']")
    private WebElementFacade submitButton;
    
    @FindBy(css = "[data-error-message]")
    private WebElementFacade errorMessage;
    
    @FindBy(css = "[data-success-message]")
    private WebElementFacade successMessage;
    
    @FindBy(css = ".form-container")
    private WebElementFacade formContainer;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = "[data-cancel-button]")
    private WebElementFacade cancelButton;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(css = ".field-error")
    private WebElementFacade fieldError;
    
    @FindBy(css = "[data-generate-seats-checkbox]")
    private WebElementFacade generateSeatsCheckbox;
    
    @FindBy(css = "#eventTime")
    private WebElementFacade eventTimeInput;
    
    @FindBy(css = "#category")
    private WebElementFacade categorySelect;
    
    /**
     * Fill the event name field
     * @param name Event name
     */
    public void fillEventName(String name) {
        waitForElement(nameInput);
        typeText(nameInput, name);
        logger.info("Filled event name: {}", name);
    }
    
    /**
     * Fill the event description field
     * @param description Event description
     */
    public void fillEventDescription(String description) {
        waitForElement(descriptionInput);
        typeText(descriptionInput, description);
        logger.info("Filled event description");
    }
    
    /**
     * Fill the event date field
     * @param eventDate Event date (YYYY-MM-DD format)
     */
    public void fillEventDate(String eventDate) {
        waitForElement(eventDateInput);
        typeText(eventDateInput, eventDate);
        logger.info("Filled event date: {}", eventDate);
    }
    
    /**
     * Fill the venue field
     * @param venue Venue name
     */
    public void fillVenue(String venue) {
        waitForElement(venueInput);
        typeText(venueInput, venue);
        logger.info("Filled venue: {}", venue);
    }
    
    /**
     * Fill the max capacity field
     * @param maxCapacity Maximum capacity
     */
    public void fillMaxCapacity(String maxCapacity) {
        waitForElement(maxCapacityInput);
        typeText(maxCapacityInput, maxCapacity);
        logger.info("Filled max capacity: {}", maxCapacity);
    }
    
    /**
     * Fill the base price field
     * @param basePrice Base ticket price
     */
    public void fillBasePrice(String basePrice) {
        waitForElement(basePriceInput);
        typeText(basePriceInput, basePrice);
        logger.info("Filled base price: {}", basePrice);
    }
    
    /**
     * Fill the event time field if available
     * @param eventTime Event time (HH:MM format)
     */
    public void fillEventTime(String eventTime) {
        if (isElementVisible(eventTimeInput)) {
            typeText(eventTimeInput, eventTime);
            logger.info("Filled event time: {}", eventTime);
        }
    }
    
    /**
     * Fill the complete event form with all required fields
     * @param name Event name
     * @param description Event description  
     * @param eventDate Event date
     * @param venue Venue name
     * @param maxCapacity Max capacity
     * @param basePrice Base price
     */
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
    
    /**
     * Fill event form with random test data
     */
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
    
    /**
     * Click the submit button to create the event
     */
    public void submitForm() {
        waitForElementClickable(submitButton);
        clickElement(submitButton);
        logger.info("Submitted event form");
    }
    
    /**
     * Click the cancel button
     */
    public void clickCancel() {
        if (isElementVisible(cancelButton)) {
            clickElement(cancelButton);
            logger.info("Clicked cancel button");
        }
    }
    
    /**
     * Complete the entire event creation process
     * @param name Event name
     * @param description Event description
     * @param eventDate Event date
     * @param venue Venue name
     * @param maxCapacity Max capacity
     * @param basePrice Base price
     */
    public void createEvent(String name, String description, String eventDate,
                           String venue, String maxCapacity, String basePrice) {
        fillEventForm(name, description, eventDate, venue, maxCapacity, basePrice);
        submitForm();
        logger.info("Created event: {}", name);
    }
    
    /**
     * Create event with test data
     */
    public void createEventWithTestData() {
        fillEventFormWithTestData();
        submitForm();
        logger.info("Created event with test data");
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
     * Check if form is displayed
     * @return true if form container is visible
     */
    public boolean isFormDisplayed() {
        return isElementVisible(formContainer);
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
     * Check if success message is displayed
     * @return true if success message is visible
     */
    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }
    
    /**
     * Get the success message text
     * @return Success message text
     */
    public String getSuccessMessage() {
        if (isElementVisible(successMessage)) {
            return getText(successMessage);
        }
        return "";
    }
    
    /**
     * Wait for form submission processing
     */
    public void waitForFormProcessing() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Form processing completed");
    }
    
    /**
     * Check if form submission was successful
     * @return true if success message is displayed
     */
    public boolean isEventCreatedSuccessfully() {
        waitForFormProcessing();
        return isSuccessMessageDisplayed() || !getCurrentUrl().contains("/create");
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
     * Toggle generate seats checkbox if available
     */
    public void toggleGenerateSeats() {
        if (isElementVisible(generateSeatsCheckbox)) {
            clickElement(generateSeatsCheckbox);
            logger.info("Toggled generate seats checkbox");
        }
    }
    
    /**
     * Check if generate seats checkbox is checked
     * @return true if checkbox is selected
     */
    public boolean isGenerateSeatsChecked() {
        if (isElementVisible(generateSeatsCheckbox)) {
            return generateSeatsCheckbox.isSelected();
        }
        return false;
    }
    
    /**
     * Get field error message
     * @return Field error text
     */
    public String getFieldError() {
        if (isElementVisible(fieldError)) {
            return getText(fieldError);
        }
        return "";
    }
    
    /**
     * Clear all form fields
     */
    public void clearAllFields() {
        if (isElementVisible(nameInput)) nameInput.clear();
        if (isElementVisible(descriptionInput)) descriptionInput.clear();
        if (isElementVisible(eventDateInput)) eventDateInput.clear();
        if (isElementVisible(venueInput)) venueInput.clear();
        if (isElementVisible(maxCapacityInput)) maxCapacityInput.clear();
        if (isElementVisible(basePriceInput)) basePriceInput.clear();
        logger.info("Cleared all form fields");
    }
    
    /**
     * Verify all required form fields are present
     * @return true if all required fields are visible
     */
    public boolean areAllRequiredFieldsPresent() {
        return isElementVisible(nameInput) &&
               isElementVisible(descriptionInput) &&
               isElementVisible(eventDateInput) &&
               isElementVisible(venueInput) &&
               isElementVisible(maxCapacityInput) &&
               isElementVisible(basePriceInput) &&
               isElementVisible(submitButton);
    }

    /**
     * Get the current event name value
     * @return Current event name
     */
    public String getCurrentEventName() {
        waitForElement(nameInput);
        return nameInput.getValue();
    }

    /**
     * Get the current event description value
     * @return Current event description
     */
    public String getCurrentEventDescription() {
        waitForElement(descriptionInput);
        return descriptionInput.getValue();
    }

    /**
     * Get event date input element
     * @return Event date input element
     */
    public WebElementFacade getEventDateInput() {
        return eventDateInput;
    }

    /**
     * Check if date picker is visible and functional
     * @return true if date picker is visible
     */
    public boolean isDatePickerVisible() {
        return isElementVisible(eventDateInput);
    }
}