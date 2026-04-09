package com.ticketing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EventApiClient {

    private static final Logger logger = LoggerFactory.getLogger(EventApiClient.class);

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
            .build();

    private static final String EVENTS_ADMIN_URL =
            System.getProperty("admin.events.api.url",
                    TestDataConfig.CATALOG_BASE_URL + "/admin/events");

    private static volatile String adminToken;

    private EventApiClient() { }

    public static void setAdminToken(String token) {
        adminToken = token;
    }

    public static String getAdminToken() {
        return adminToken;
    }

    public static boolean eventExists(String eventName) {
        try {
            HttpResponse<String> response = doGet(EVENTS_ADMIN_URL);
            if (response.statusCode() == 200) {
                boolean exists = response.body().contains(eventName);
                logger.info("Event '{}' exists check: {}", eventName, exists);
                return exists;
            }
            logger.debug("Events list returned HTTP {}", response.statusCode());
            return false;
        } catch (Exception e) {
            logger.warn("Could not check event existence: {}", e.getMessage());
            return false;
        }
    }

    public static String getEventId(String eventName) {
        try {
            HttpResponse<String> response = doGet(EVENTS_ADMIN_URL);
            if (response.statusCode() != 200) {
                logger.warn("Could not list events (HTTP {})", response.statusCode());
                return null;
            }
            return extractIdByName(response.body(), eventName);
        } catch (Exception e) {
            logger.warn("Could not get event ID for '{}': {}", eventName, e.getMessage());
            return null;
        }
    }

    public static String createEvent(String name,
                                     String description,
                                     String eventDate,
                                     String venue,
                                     String maxCapacity,
                                     String basePrice) {

        String body = String.format(
                "{\"name\":\"%s\",\"description\":\"%s\","
                + "\"eventDate\":\"%s\",\"venue\":\"%s\","
                + "\"maxCapacity\":%s,\"basePrice\":%s}",
                name, description, eventDate, venue, maxCapacity, basePrice);

        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(EVENTS_ADMIN_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS));
            if (adminToken != null) {
                reqBuilder.header("Authorization", "Bearer " + adminToken);
            }
            HttpRequest request = reqBuilder.build();

            HttpResponse<String> response =
                    HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                logger.info("Event created via API: {} (HTTP {})", name, status);
                return extractId(response.body());
            } else if (status == 409) {
                logger.info("Event already exists (409): {}", name);
                return getEventId(name);
            } else {
                logger.warn("Event API returned HTTP {} for '{}': {}",
                        status, name, response.body());
                return null;
            }
        } catch (Exception e) {
            logger.warn("Could not create event via API ({}): {}",
                    EVENTS_ADMIN_URL, e.getMessage());
            return null;
        }
    }

    public static boolean generateSeats(String eventId) {
        String seatsUrl = EVENTS_ADMIN_URL + "/" + eventId + "/seats";

        String body = "{\"sectionConfigurations\":["
                + "{\"sectionCode\":\"VIP\",\"rows\":5,\"seatsPerRow\":10,\"priceMultiplier\":2.0},"
                + "{\"sectionCode\":\"General\",\"rows\":10,\"seatsPerRow\":20,\"priceMultiplier\":1.0}"
                + "]}";

        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(seatsUrl))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS));
            if (adminToken != null) {
                reqBuilder.header("Authorization", "Bearer " + adminToken);
            }
            HttpRequest request = reqBuilder.build();

            HttpResponse<String> response =
                    HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                logger.info("Seats generated for event {} (HTTP {})", eventId, status);
                return true;
            } else if (status == 409 || status == 400) {
                logger.info("Seats already exist for event {} (idempotent)", eventId);
                return true;
            } else {
                logger.warn("Seats API returned HTTP {} for event {}: {}",
                        status, eventId, response.body());
                return false;
            }
        } catch (Exception e) {
            logger.warn("Could not generate seats for event {}: {}", eventId, e.getMessage());
            return false;
        }
    }

    public static boolean createWaitlistEventIfAbsent() {
        String name = TestDataConfig.WAITLIST_EVENT_NAME;

        String eventId;

        if (eventExists(name)) {
            logger.info("Event '{}' already exists — fetching ID.", name);
            eventId = getEventId(name);
        } else {
            eventId = createEvent(
                    name,
                    TestDataConfig.WAITLIST_EVENT_DESCRIPTION,
                    TestDataConfig.WAITLIST_EVENT_DATE,
                    TestDataConfig.WAITLIST_EVENT_VENUE,
                    TestDataConfig.WAITLIST_EVENT_MAX_CAPACITY,
                    TestDataConfig.WAITLIST_EVENT_BASE_PRICE
            );
        }

        if (eventId == null) {
            logger.error("Could not obtain event ID for '{}'", name);
            return false;
        }

        logger.info("Event '{}' ID = {}", name, eventId);

        return generateSeats(eventId);
    }

    private static HttpResponse<String> doGet(String url) throws Exception {
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS));
        if (adminToken != null) {
            reqBuilder.header("Authorization", "Bearer " + adminToken);
        }
        return HTTP.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static String extractId(String json) {
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([0-9a-fA-F-]{36})\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        Pattern numPattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Matcher numMatcher = numPattern.matcher(json);
        if (numMatcher.find()) {
            return numMatcher.group(1);
        }
        logger.warn("Could not extract event ID from response: {}",
                json.substring(0, Math.min(json.length(), 200)));
        return null;
    }

    private static String extractIdByName(String json, String eventName) {
        String[] chunks = json.split("\\{");
        for (String chunk : chunks) {
            if (chunk.contains(eventName)) {
                String idValue = extractId("{" + chunk);
                if (idValue != null) {
                    return idValue;
                }
            }
        }
        logger.warn("Event '{}' not found in events list", eventName);
        return null;
    }
}
