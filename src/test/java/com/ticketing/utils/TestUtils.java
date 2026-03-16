package com.ticketing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * TestUtils class containing utility methods for test execution
 */
public class TestUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    private static final Random random = new Random();
    
    /**
     * Get base URL for the application
     * @return Base URL from configuration
     */
    public static String getBaseUrl() {
        return System.getProperty("ticketing.url", "http://localhost:3000");
    }
    
    /**
     * Get admin URL for the application
     * @return Admin URL from configuration
     */
    public static String getAdminUrl() {
        return System.getProperty("admin.url", "http://localhost:3000/admin");
    }
    
    /**
     * Get admin email for testing
     * @return Admin email from configuration
     */
    public static String getAdminEmail() {
        return System.getProperty("test.admin.email", "admin@ticketing.com");
    }
    
    /**
     * Get admin password for testing
     * @return Admin password from configuration
     */
    public static String getAdminPassword() {
        return System.getProperty("test.admin.password", "admin123");
    }
    
    /**
     * Generate a unique event name for testing
     * @return Unique event name
     */
    public static String generateUniqueEventName() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        int randomNumber = random.nextInt(1000);
        return "Test Event " + timestamp + randomNumber;
    }
    
    /**
     * Generate a future date string
     * @param daysFromNow Number of days from today
     * @return Date string in format YYYY-MM-DD
     */
    public static String generateFutureDate(int daysFromNow) {
        LocalDate futureDate = LocalDate.now().plusDays(daysFromNow);
        return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    /**
     * Generate random venue name
     * @return Random venue name
     */
    public static String generateRandomVenue() {
        String[] venues = {
            "Madison Square Garden",
            "Staples Center", 
            "Red Rocks Amphitheatre",
            "The Forum",
            "Hollywood Bowl",
            "Toyota Center"
        };
        return venues[random.nextInt(venues.length)];
    }
    
    /**
     * Generate random ticket price
     * @return Random price between 25 and 200
     */
    public static String generateRandomPrice() {
        int price = random.nextInt(175) + 25; // Between 25 and 200
        return String.valueOf(price);
    }
    
    /**
     * Generate random capacity
     * @return Random capacity between 100 and 5000
     */
    public static String generateRandomCapacity() {
        int capacity = random.nextInt(4900) + 100; // Between 100 and 5000
        return String.valueOf(capacity);
    }
    
    /**
     * Clean up text by removing extra whitespaces and special characters
     * @param text Text to clean
     * @return Cleaned text
     */
    public static String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Wait for a specified number of seconds
     * @param seconds Number of seconds to wait
     */
    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.debug("Waited {} seconds", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Log test step information
     * @param stepDescription Description of the test step
     */
    public static void logTestStep(String stepDescription) {
        logger.info("TEST STEP: {}", stepDescription);
    }
    
    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}