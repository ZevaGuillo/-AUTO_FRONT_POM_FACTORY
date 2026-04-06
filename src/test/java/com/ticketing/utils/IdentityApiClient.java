package com.ticketing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Lightweight HTTP client for Identity Service (localhost:50000).
 *
 * Responsibilities:
 *  • Create users        (POST /users)
 *  • Obtain JWT tokens   (POST /token)
 *
 * Stateless — every method receives the parameters it needs.
 * Uses java.net.http.HttpClient (built-in since Java 11).
 */
public final class IdentityApiClient {

    private static final Logger logger = LoggerFactory.getLogger(IdentityApiClient.class);

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
            .build();

    private IdentityApiClient() { /* utility class */ }

    // ── Create user ────────────────────────────────────────────────────────

    /**
     * POST /register — creates a new user.
     * Returns true if 201 (created) or 400 (already exists — idempotent).
     */
    public static boolean createUser(String email, String password, String role) {
        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                email, password, role);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestDataConfig.IDENTITY_BASE_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (status == 201) {
                logger.info("User created: {} (role={})", email, role);
                return true;
            } else if (status == 400) {
                logger.info("User already exists (idempotent): {}", email);
                return true;
            } else {
                logger.error("Failed to create user {} — HTTP {} : {}", email, status, response.body());
                return false;
            }
        } catch (Exception e) {
            logger.error("Identity service unreachable when creating user {}: {}", email, e.getMessage());
            return false;
        }
    }

    // ── Get token ──────────────────────────────────────────────────────────

    /**
     * POST /token — exchanges email + password for a JWT.
     * Returns the raw token string, or null on failure.
     */
    public static String getToken(String email, String password) {
        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}",
                email, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestDataConfig.IDENTITY_BASE_URL + "/token"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Minimal JSON extraction — avoids external dependency
                String token = extractJsonValue(response.body(), "token");
                logger.info("Token obtained for {}", email);
                return token;
            } else {
                logger.error("Failed to get token for {} — HTTP {} : {}",
                        email, response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logger.error("Identity service unreachable when getting token: {}", e.getMessage());
            return null;
        }
    }

    // ── Health check ───────────────────────────────────────────────────────

    /**
     * GET /health — quick connectivity test.
     */
    public static boolean isHealthy() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestDataConfig.IDENTITY_BASE_URL + "/health"))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            logger.warn("Identity service health check failed: {}", e.getMessage());
            return false;
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Extracts a simple string value from a flat JSON object.
     * Example: {"token":"abc123"} → extractJsonValue(json, "token") → "abc123"
     */
    static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"";
        int keyIdx = json.indexOf(pattern);
        if (keyIdx == -1) return null;

        int colonIdx = json.indexOf(':', keyIdx + pattern.length());
        if (colonIdx == -1) return null;

        int startQuote = json.indexOf('"', colonIdx + 1);
        if (startQuote == -1) return null;

        int endQuote = json.indexOf('"', startQuote + 1);
        if (endQuote == -1) return null;

        return json.substring(startQuote + 1, endQuote);
    }
}
