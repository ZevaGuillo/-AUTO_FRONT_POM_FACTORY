package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the Edit Event page
 * URL: /admin/events/{eventId}/edit
 */
@DefaultUrl("/admin/events/edit")
public class EditEventPage extends BasePage {
    
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
    private WebElementFacade updateButton;
    
    @FindBy(css = "[data-cancel-button]")
    private WebElementFacade cancelButton;
    
    @FindBy(css = "[data-delete-button]")
    private WebElementFacade deleteButton;
    
    @FindBy(css = "[data-error-message]")
    private WebElementFacade errorMessage;
    
    @FindBy(css = "[data-success-message]")
    private WebElementFacade successMessage;
    
    @FindBy(css = ".form-container")
    private WebElementFacade formContainer;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(css = ".delete-confirmation-modal")
    private WebElementFacade deleteConfirmationModal;
    
    @FindBy(css = "[data-confirm-delete]")
    private WebElementFacade confirmDeleteButton;
    
    @FindBy(css = "[data-cancel-delete]")
    private WebElementFacade cancelDeleteButton;
    
    @FindBy(css = "[data-event-status]")
    private WebElementFacade eventStatusSelect;
    
    /**
     * Update the event name field
     * @param name New event name
     */
    public void updateEventName(String name) {
        waitForElement(nameInput);
        nameInput.clear();
        typeText(nameInput, name);
        logger.info("Updated event name to: {}", name);
    }
    
    /**
     * Update the event description field
     * @param description New event description
     */
    public void updateEventDescription(String description) {
        waitForElement(descriptionInput);
        descriptionInput.clear();
        typeText(descriptionInput, description);
        logger.info("Updated event description");
    }
    
    /**
     * Update the event date field
     * @param eventDate New event date (YYYY-MM-DD format)
     */
    public void updateEventDate(String eventDate) {
        waitForElement(eventDateInput);
        eventDateInput.clear();
        typeText(eventDateInput, eventDate);
        logger.info("Updated event date to: {}", eventDate);
    }
    
    /**
     * Update the venue field
     * @param venue New venue name
     */
    public void updateVenue(String venue) {
        waitForElement(venueInput);
        venueInput.clear();
        typeText(venueInput, venue);
        logger.info("Updated venue to: {}", venue);
    }
    
    /**
     * Update the max capacity field
     * @param maxCapacity New maximum capacity
     */
    public void updateMaxCapacity(String maxCapacity) {
        waitForElement(maxCapacityInput);
        maxCapacityInput.clear();
        typeText(maxCapacityInput, maxCapacity);
        logger.info("Updated max capacity to: {}", maxCapacity);
    }
    
    /**
     * Update the base price field
     * @param basePrice New base ticket price
     */
    public void updateBasePrice(String basePrice) {
        waitForElement(basePriceInput);
        basePriceInput.clear();
        typeText(basePriceInput, basePrice);
        logger.info("Updated base price to: {}", basePrice);
    }
    
    /**
     * Click the update button to save changes
     */
    public void clickUpdate() {
        waitForElementClickable(updateButton);
        clickElement(updateButton);
        logger.info("Clicked update button");
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
     * Click the delete button to initiate deletion
     */
    public void clickDelete() {
        if (isElementVisible(deleteButton)) {
            clickElement(deleteButton);
            logger.info("Clicked delete button");
        }
    }
    
    /**
     * Confirm event deletion in the modal
     */
    public void confirmDelete() {
        if (isElementVisible(confirmDeleteButton)) {
            clickElement(confirmDeleteButton);
            logger.info("Confirmed event deletion");
        }
    }
    
    /**
     * Cancel event deletion in the modal
     */
    public void cancelDelete() {
        if (isElementVisible(cancelDeleteButton)) {
            clickElement(cancelDeleteButton);
            logger.info("Cancelled event deletion");
        }
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
     * Get the page title
     * @return Page title text
     */
    public String getPageTitle() {
        waitForElement(pageTitle);
        return getText(pageTitle);
    }
    
    /**
     * Check if edit form is displayed
     * @return true if form container is visible
     */
    public boolean isEditFormDisplayed() {
        return isElementVisible(formContainer);
    }
    
    /**
     * Check if delete confirmation modal is displayed
     * @return true if modal is visible
     */
    public boolean isDeleteConfirmationModalDisplayed() {
        return isElementVisible(deleteConfirmationModal);
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
     * Wait for form processing to complete
     */
    public void waitForFormProcessing() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Form processing completed");
    }
    
    /**
     * Check if update was successful
     * @return true if success message is displayed
     */
    public boolean isUpdateSuccessful() {
        waitForFormProcessing();
        return isSuccessMessageDisplayed();
    }
    
    /**
     * Check if event was deleted successfully
     * @return true if redirected away from edit page
     */
    public boolean isDeleteSuccessful() {
        waitForFormProcessing();
        return !getCurrentUrl().contains("/edit");
    }
    
    /**
     * Update event with partial information
     * @param name New name (null to skip)
     * @param description New description (null to skip)
     * @param venue New venue (null to skip)
     * @param price New price (null to skip)
     */
    public void updateEventPartial(String name, String description, String venue, String price) {
        if (name != null) updateEventName(name);
        if (description != null) updateEventDescription(description);
        if (venue != null) updateVenue(venue);
        if (price != null) updateBasePrice(price);
        
        clickUpdate();
        logger.info("Updated event with partial information");
    }
    
    /**
     * Delete the event with confirmation
     */
    public void deleteEventWithConfirmation() {
        clickDelete();
        if (isDeleteConfirmationModalDisplayed()) {
            confirmDelete();
        }
        logger.info("Deleted event with confirmation");
    }
    
    /**
     * Check if update button is enabled
     * @return true if update button is enabled
     */
    public boolean isUpdateButtonEnabled() {
        waitForElement(updateButton);
        return updateButton.isEnabled();
    }
    
    /**
     * Verify all edit form fields are populated
     * @return true if all fields have values
     */
    public boolean areAllFieldsPopulated() {
        return !getCurrentEventName().isEmpty() &&
               !getCurrentEventDescription().isEmpty() &&
               !venueInput.getValue().isEmpty() &&
               !basePriceInput.getValue().isEmpty();
    }
}