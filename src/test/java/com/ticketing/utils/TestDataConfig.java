package com.ticketing.utils;

public final class TestDataConfig {

    private TestDataConfig() { }

    public static final String GATEWAY_BASE_URL =
            System.getProperty("gateway.api.url", "http://localhost:5000");

    public static final String IDENTITY_BASE_URL = GATEWAY_BASE_URL + "/auth";
    public static final String CATALOG_BASE_URL  = GATEWAY_BASE_URL + "/catalog";
    public static final String WAITLIST_BASE_URL  = GATEWAY_BASE_URL + "/api/waitlist";

    public static final String FRONTEND_BASE_URL =
            System.getProperty("ticketing.url", "http://localhost:3000");

    public static final String TEST_USER_EMAIL =
            System.getProperty("test.user.email", "waitlistuser@test.com");

    public static final String TEST_USER_PASSWORD =
            System.getProperty("test.user.password", "Test1234!");

    public static final String TEST_USER_ROLE = "User";

    public static final String RESERVE_USER_EMAIL =
            System.getProperty("test.reserve.email", "reserveuser@test.com");

    public static final String RESERVE_USER_PASSWORD =
            System.getProperty("test.reserve.password", "Reserve1234!");

    public static final String RESERVE_USER_ROLE = "User";

    public static final String RESERVE_SECTION = "General";
    public static final int RESERVE_ROW = 1;
    public static final int RESERVE_SEAT = 1;

    public static final String ADMIN_EMAIL =
            System.getProperty("test.admin.email", "admin@ticketing.com");

    public static final String ADMIN_PASSWORD =
            System.getProperty("test.admin.password", "Admin123!");

    public static final String ADMIN_ROLE = "Admin";

    public static final String WAITLIST_EVENT_NAME = "Concierto Sinfónico";
    public static final String WAITLIST_EVENT_DESCRIPTION =
            "Evento de prueba para validar funcionalidad de lista de espera";
    public static final String WAITLIST_EVENT_DATE = "2026-12-31T20:00:00Z";
    public static final String WAITLIST_EVENT_VENUE = "Teatro Nacional";
    public static final String WAITLIST_EVENT_MAX_CAPACITY = "500";
    public static final String WAITLIST_EVENT_BASE_PRICE = "50";

    public static final int HTTP_TIMEOUT_SECONDS = 10;
}
