package com.ticketing.pages.admin;

import com.ticketing.pages.BasePage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Manage Seats page
 * URL: /admin/events/[eventId]/seats
 */
@DefaultUrl("/admin/events")
public class ManageSeatsPage extends BasePage {
    
    @FindBy(css = ".seatmap-editor")
    private WebElementFacade seatmapEditor;
    
    @FindBy(css = "[data-generate-seats-button]")
    private WebElementFacade generateSeatsButton;
    
    @FindBy(css = "[data-save-layout-button]")
    private WebElementFacade saveLayoutButton;
    
    @FindBy(css = "[data-reset-layout-button]")
    private WebElementFacade resetLayoutButton;
    
    @FindBy(css = ".seat-editor")
    private List<WebElementFacade> seatEditors;
    
    @FindBy(css = "[data-rows-input]")
    private WebElementFacade rowsInput;
    
    @FindBy(css = "[data-seats-per-row-input]")
    private WebElementFacade seatsPerRowInput;
    
    @FindBy(css = "[data-section-name-input]")
    private WebElementFacade sectionNameInput;
    
    @FindBy(css = "[data-seat-price-input]")
    private WebElementFacade seatPriceInput;
    
    @FindBy(css = ".seat-configuration")
    private WebElementFacade seatConfiguration;
    
    @FindBy(css = ".generated-seats")
    private WebElementFacade generatedSeatsContainer;
    
    @FindBy(css = ".seat-item")
    private List<WebElementFacade> seatItems;
    
    @FindBy(css = "[data-total-seats-count]")
    private WebElementFacade totalSeatsCount;
    
    @FindBy(css = "[data-available-seats-count]")
    private WebElementFacade availableSeatsCount;
    
    @FindBy(css = "[data-success-message]")
    private WebElementFacade successMessage;
    
    @FindBy(css = "[data-error-message]")
    private WebElementFacade errorMessage;
    
    @FindBy(css = "h1")
    private WebElementFacade pageTitle;
    
    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;
    
    @FindBy(css = "[data-seat-type-select]")
    private WebElementFacade seatTypeSelect;
    
    @FindBy(css = "[data-bulk-action-select]")
    private WebElementFacade bulkActionSelect;
    
    @FindBy(css = "[data-apply-bulk-action]")
    private WebElementFacade applyBulkActionButton;
    
    @FindBy(css = ".seat-legend")
    private WebElementFacade seatLegend;
    
    /**
     * Set number of rows for seat generation
     * @param rows Number of rows
     */
    public void setRows(String rows) {
        waitForElement(rowsInput);
        typeText(rowsInput, rows);
        logger.info("Set rows to: {}", rows);
    }
    
    /**
     * Set number of seats per row
     * @param seatsPerRow Number of seats per row
     */
    public void setSeatsPerRow(String seatsPerRow) {
        waitForElement(seatsPerRowInput);
        typeText(seatsPerRowInput, seatsPerRow);
        logger.info("Set seats per row to: {}", seatsPerRow);
    }
    
    /**
     * Set section name
     * @param sectionName Name of the section
     */
    public void setSectionName(String sectionName) {
        if (isElementVisible(sectionNameInput)) {
            typeText(sectionNameInput, sectionName);
            logger.info("Set section name to: {}", sectionName);
        }
    }
    
    /**
     * Set price for seats
     * @param price Seat price
     */
    public void setSeatPrice(String price) {
        if (isElementVisible(seatPriceInput)) {
            typeText(seatPriceInput, price);
            logger.info("Set seat price to: {}", price);
        }
    }
    
    /**
     * Click generate seats button
     */
    public void clickGenerateSeats() {
        waitForElementClickable(generateSeatsButton);
        clickElement(generateSeatsButton);
        logger.info("Clicked generate seats button");
    }
    
    /**
     * Click save layout button
     */
    public void clickSaveLayout() {
        waitForElementClickable(saveLayoutButton);
        clickElement(saveLayoutButton);
        logger.info("Clicked save layout button");
    }
    
    /**
     * Click reset layout button
     */
    public void clickResetLayout() {
        if (isElementVisible(resetLayoutButton)) {
            clickElement(resetLayoutButton);
            logger.info("Clicked reset layout button");
        }
    }
    
    /**
     * Generate seats with specified configuration
     * @param rows Number of rows
     * @param seatsPerRow Seats per row
     * @param sectionName Section name (optional)
     * @param price Seat price (optional)
     */
    public void generateSeats(String rows, String seatsPerRow, String sectionName, String price) {
        setRows(rows);
        setSeatsPerRow(seatsPerRow);
        if (sectionName != null) setSectionName(sectionName);
        if (price != null) setSeatPrice(price);
        
        clickGenerateSeats();
        logger.info("Generated seats configuration: {}x{}", rows, seatsPerRow);
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
     * Check if seatmap editor is displayed
     * @return true if seatmap editor is visible
     */
    public boolean isSeatmapEditorDisplayed() {
        return isElementVisible(seatmapEditor);
    }
    
    /**
     * Check if seat configuration panel is displayed
     * @return true if configuration panel is visible
     */
    public boolean isSeatConfigurationDisplayed() {
        return isElementVisible(seatConfiguration);
    }
    
    /**
     * Get total seats count
     * @return Total seats count text
     */
    public String getTotalSeatsCount() {
        if (isElementVisible(totalSeatsCount)) {
            return getText(totalSeatsCount);
        }
        return "0";
    }
    
    /**
     * Get available seats count
     * @return Available seats count text
     */
    public String getAvailableSeatsCount() {
        if (isElementVisible(availableSeatsCount)) {
            return getText(availableSeatsCount);
        }
        return "0";
    }
    
    /**
     * Get number of generated seat items
     * @return Number of seat items displayed
     */
    public int getGeneratedSeatsCount() {
        return seatItems.size();
    }
    
    /**
     * Check if seats have been generated
     * @return true if generated seats container is visible and has seats
     */
    public boolean areSeatsGenerated() {
        return isElementVisible(generatedSeatsContainer) && !seatItems.isEmpty();
    }
    
    /**
     * Check if success message is displayed
     * @return true if success message is visible
     */
    public boolean isSuccessMessageDisplayed() {
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
     * Check if error message is displayed
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
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
     * Wait for seat generation processing
     */
    public void waitForSeatGeneration() {
        if (isElementVisible(loadingSpinner)) {
            loadingSpinner.waitUntilNotVisible();
        }
        waitForPageLoad();
        logger.info("Seat generation processing completed");
    }
    
    /**
     * Check if seat generation was successful
     * @return true if seats were generated successfully
     */
    public boolean isSeatGenerationSuccessful() {
        waitForSeatGeneration();
        return areSeatsGenerated() || isSuccessMessageDisplayed();
    }
    
    /**
     * Click on a specific seat item by index
     * @param index Index of the seat to click (0-based)
     */
    public void clickSeatByIndex(int index) {
        if (index < seatItems.size()) {
            WebElementFacade seat = seatItems.get(index);
            clickElement(seat);
            logger.info("Clicked seat at index: {}", index);
        }
    }
    
    /**
     * Apply bulk action to seats
     * @param action The bulk action to apply
     */
    public void applyBulkAction(String action) {
        if (isElementVisible(bulkActionSelect)) {
            bulkActionSelect.selectByVisibleText(action);
            clickElement(applyBulkActionButton);
            logger.info("Applied bulk action: {}", action);
        }
    }
    
    /**
     * Save the current seat layout
     */
    public void saveSeatLayout() {
        clickSaveLayout();
        waitForSeatGeneration();
        logger.info("Saved seat layout");
    }
    
    /**
     * Check if seat legend is displayed
     * @return true if seat legend is visible
     */
    public boolean isSeatLegendDisplayed() {
        return isElementVisible(seatLegend);
    }
    
    /**
     * Reset seat layout to default
     */
    public void resetSeatLayout() {
        clickResetLayout();
        waitForSeatGeneration();
        logger.info("Reset seat layout");
    }
    
    /**
     * Verify all seat management controls are present
     * @return true if all controls are displayed
     */
    public boolean areAllControlsPresent() {
        return isElementVisible(rowsInput) &&
               isElementVisible(seatsPerRowInput) &&
               isElementVisible(generateSeatsButton) &&
               isElementVisible(saveLayoutButton);
    }
    
    /**
     * Generate default seat layout for testing
     */
    public void generateDefaultSeatLayout() {
        generateSeats("10", "20", "Main Section", "50");
        saveSeatLayout();
        logger.info("Generated default seat layout for testing");
    }
}