package com.ticketing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client for Waitlist endpoints through the API Gateway.
 *
 * <pre>
 * POST   /api/waitlist/join                  — Join waitlist
 * GET    /api/waitlist/status                — Get waitlist status
 * DELETE /api/waitlist/cancel                — Cancel waitlist entry
 * GET    /api/waitlist/opportunity/{token}   — Validate opportunity token
 * GET    /api/waitlist/my-opportunities      — Get user opportunities
 * </pre>
 *
 * All calls require a user JWT in the Authorization header.
 */
public final class WaitlistApiClient {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistApiClient.class);

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
            .build();

    private WaitlistApiClient() { /* utility class */ }

    // ── Cancel waitlist entry ──────────────────────────────────────────────

    /**
     * DELETE /api/waitlist/cancel — cancels the user's waitlist entry.
     *
     * @param userToken JWT of the user whose entry should be cancelled.
     * @return true if 2xx or 404 (no entry to cancel — idempotent).
     */
    public static boolean cancelEntry(String userToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestDataConfig.WAITLIST_BASE_URL + "/cancel"))
                    .header("Authorization", "Bearer " + userToken)
                    .header("Accept", "application/json")
                    .DELETE()
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
                    .build();

            HttpResponse<String> response =
                    HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                logger.info("Waitlist entry cancelled successfully");
                return true;
            } else if (status == 404) {
                logger.info("No waitlist entry to cancel (idempotent)");
                return true;
            } else {
                logger.warn("Cancel waitlist returned HTTP {}: {}", status, response.body());
                return false;
            }
        } catch (Exception e) {
            logger.warn("Could not cancel waitlist entry: {}", e.getMessage());
            return false;
        }
    }

    // ── Get waitlist status ────────────────────────────────────────────────

    /**
     * GET /api/waitlist/status — checks the user's current waitlist status.
     *
     * @param userToken JWT of the user.
     * @return response body (JSON) or null on failure.
     */
    public static String getStatus(String userToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestDataConfig.WAITLIST_BASE_URL + "/status"))
                    .header("Authorization", "Bearer " + userToken)
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(TestDataConfig.HTTP_TIMEOUT_SECONDS))
                    .build();

            HttpResponse<String> response =
                    HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                logger.debug("Waitlist status returned HTTP {}", response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.warn("Could not get waitlist status: {}", e.getMessage());
            return null;
        }
    }
}
