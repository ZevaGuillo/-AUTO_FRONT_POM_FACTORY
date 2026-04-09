package com.ticketing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    private static final Random random = new Random();
    
    public static String getBaseUrl() {
        return System.getProperty("ticketing.url", "http://localhost:3000");
    }
    
    public static String getAdminUrl() {
        return System.getProperty("admin.url", "http://localhost:3000/admin");
    }

    public static String getAdminEmail() {
        return System.getProperty("test.admin.email", "admin@ticketing.com");
    }

    public static String getAdminPassword() {
        return System.getProperty("test.admin.password", "Admin123!");
    }
    
    public static String generateUniqueEventName() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        int randomNumber = random.nextInt(1000);
        return "Test Event " + timestamp + randomNumber;
    }
    
    public static String generateFutureDate(int daysFromNow) {
        LocalDate futureDate = LocalDate.now().plusDays(daysFromNow);
        return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
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

    public static String generateRandomPrice() {
        int price = random.nextInt(175) + 25;
        return String.valueOf(price);
    }

    public static String generateRandomCapacity() {
        int capacity = random.nextInt(4900) + 100;
        return String.valueOf(capacity);
    }

    public static String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.debug("Waited {} seconds", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Wait interrupted: {}", e.getMessage());
        }
    }

    public static void logTestStep(String stepDescription) {
        logger.info("TEST STEP: {}", stepDescription);
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
