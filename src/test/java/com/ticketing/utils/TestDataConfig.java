package com.ticketing.utils;

/**
 * Centralised configuration for API endpoints and test credentials.
 * Values come from system properties first, falling back to sensible defaults.
 *
 * No business logic — only constants / property look-ups.
 */
public final class TestDataConfig {

    private TestDataConfig() { /* utility class */ }

    // ── API Gateway ────────────────────────────────────────────────────────
    public static final String GATEWAY_BASE_URL =
            System.getProperty("gateway.api.url", "http://localhost:5000");

    // Routes through the gateway (see appsettings.json ReverseProxy config)
    public static final String IDENTITY_BASE_URL = GATEWAY_BASE_URL + "/auth";
    public static final String CATALOG_BASE_URL  = GATEWAY_BASE_URL + "/catalog";
    public static final String WAITLIST_BASE_URL  = GATEWAY_BASE_URL + "/api/waitlist";

    public static final String FRONTEND_BASE_URL =
            System.getProperty("ticketing.url", "http://localhost:3000");

    // ── Test user for waitlist scenarios ────────────────────────────────────
    public static final String TEST_USER_EMAIL =
            System.getProperty("test.user.email", "waitlistuser@test.com");

    public static final String TEST_USER_PASSWORD =
            System.getProperty("test.user.password", "Test1234!");

    public static final String TEST_USER_ROLE = "User";

    // ── Reserve user (User A — reserves a seat so User B sees it as Reserved)
    public static final String RESERVE_USER_EMAIL =
            System.getProperty("test.reserve.email", "reserveuser@test.com");

    public static final String RESERVE_USER_PASSWORD =
            System.getProperty("test.reserve.password", "Reserve1234!");

    public static final String RESERVE_USER_ROLE = "User";

    // Seat that User A reserves (User B will see it as Reserved and trigger waitlist)
    public static final String RESERVE_SECTION = "General";
    public static final int RESERVE_ROW = 1;
    public static final int RESERVE_SEAT = 1;

    // ── Admin user (already seeded by admin tests / infra) ─────────────────
    public static final String ADMIN_EMAIL =
            System.getProperty("test.admin.email", "admin@ticketing.com");

    public static final String ADMIN_PASSWORD =
            System.getProperty("test.admin.password", "Admin123!");

    public static final String ADMIN_ROLE = "Admin";

    // ── Event data for waitlist tests ──────────────────────────────────────
    public static final String WAITLIST_EVENT_NAME = "Concierto Sinfónico";
    public static final String WAITLIST_EVENT_DESCRIPTION =
            "Evento de prueba para validar funcionalidad de lista de espera";
    public static final String WAITLIST_EVENT_DATE = "2026-12-31T20:00:00Z";
    public static final String WAITLIST_EVENT_VENUE = "Teatro Nacional";
    public static final String WAITLIST_EVENT_MAX_CAPACITY = "500";
    public static final String WAITLIST_EVENT_BASE_PRICE = "50";

    // ── HTTP timeouts ──────────────────────────────────────────────────────
    public static final int HTTP_TIMEOUT_SECONDS = 10;
}
