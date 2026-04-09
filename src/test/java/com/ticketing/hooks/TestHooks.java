package com.ticketing.hooks;

import com.ticketing.utils.EventApiClient;
import com.ticketing.utils.IdentityApiClient;
import com.ticketing.utils.TestDataConfig;
import com.ticketing.utils.WaitlistApiClient;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber hooks for test data setup and teardown.
 *
 * <h3>Design decisions</h3>
 * <ul>
 *   <li><b>Separation of concerns</b>: hooks only call API clients
 *       ({@link IdentityApiClient}, {@link EventApiClient}, {@link WaitlistApiClient}).
 *       They never import or reference Page Objects.</li>
 *   <li><b>Idempotency</b>: both @Before and @After can run
 *       multiple times without side effects.</li>
 *   <li><b>Independence</b>: @After cancels any waitlist entry so the
 *       next scenario starts clean.</li>
 *   <li><b>Tag-scoped</b>: heavy setup only applies to
 *       {@code @waitlist} scenarios; other features stay fast.</li>
 * </ul>
 *
 * <h3>API Gateway routes used</h3>
 * <pre>
 *   POST   /auth/register                — Create user
 *   POST   /auth/token                   — Get JWT
 *   GET    /auth/health                  — Health check
 *   POST   /catalog/admin/events         — Create event (no auth)
 *   DELETE /api/waitlist/cancel           — Cancel waitlist entry (cleanup)
 * </pre>
 */
public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);

    /** One-time data seeding guard. */
    private static volatile boolean dataSeeded = false;

    /** Cached user token for @After cleanup. */
    private static volatile String testUserToken = null;

    // ====================================================================
    // @Before — runs BEFORE every @waitlist scenario
    // ====================================================================

    @Before(value = "@waitlist", order = 1)
    public void setupWaitlistTestData(Scenario scenario) {
        logger.info("═══ @Before [waitlist] — {} ═══", scenario.getName());

        if (dataSeeded) {
            logger.info("Test data already seeded. Skipping API calls.");
            return;
        }

        // 1. Health check  (GET /auth/health)
        if (!IdentityApiClient.isHealthy()) {
            logger.error("Identity Service unreachable at {}", TestDataConfig.IDENTITY_BASE_URL);
            throw new IllegalStateException(
                "Identity Service unreachable: " + TestDataConfig.IDENTITY_BASE_URL);
        }
        logger.info("Identity Service healthy ✓");

        // 2. Create test user  (POST /auth/register)
        boolean userCreated = IdentityApiClient.createUser(
            TestDataConfig.TEST_USER_EMAIL,
            TestDataConfig.TEST_USER_PASSWORD,
            TestDataConfig.TEST_USER_ROLE);
        if (!userCreated) {
            throw new IllegalStateException(
                "Could not create test user: " + TestDataConfig.TEST_USER_EMAIL);
        }
        logger.info("Test user ready: {} ✓", TestDataConfig.TEST_USER_EMAIL);

        // 3. Cache user token for @After cleanup  (POST /auth/token)
        testUserToken = IdentityApiClient.getToken(
            TestDataConfig.TEST_USER_EMAIL,
            TestDataConfig.TEST_USER_PASSWORD);
        if (testUserToken != null) {
            logger.info("Test user token cached for cleanup ✓");
        }

        // 4. Create reserve user (User A — will reserve a seat so User B sees 'Reserved')
        boolean reserveUserCreated = IdentityApiClient.createUser(
            TestDataConfig.RESERVE_USER_EMAIL,
            TestDataConfig.RESERVE_USER_PASSWORD,
            TestDataConfig.RESERVE_USER_ROLE);
        if (!reserveUserCreated) {
            throw new IllegalStateException(
                "Could not create reserve user: " + TestDataConfig.RESERVE_USER_EMAIL);
        }
        logger.info("Reserve user ready: {} ✓", TestDataConfig.RESERVE_USER_EMAIL);

        // 5. Create admin user  (POST /auth/register)
        IdentityApiClient.createUser(
            TestDataConfig.ADMIN_EMAIL,
            TestDataConfig.ADMIN_PASSWORD,
            TestDataConfig.ADMIN_ROLE);
        logger.info("Admin user ready: {} ✓", TestDataConfig.ADMIN_EMAIL);

        // 5b. Get admin token for catalog admin API calls
        String adminToken = IdentityApiClient.getToken(
            TestDataConfig.ADMIN_EMAIL,
            TestDataConfig.ADMIN_PASSWORD);
        if (adminToken != null) {
            EventApiClient.setAdminToken(adminToken);
            logger.info("Admin token set for EventApiClient ✓");
        } else {
            logger.warn("Could not obtain admin token — event creation may fail.");
        }

        // NOTA: La generación de asientos y reserva se realiza en el step definition para permitir 1 asiento reservado por sección.

        dataSeeded = true;
        logger.info("═══ Data seeding complete ═══");
    }

    // ====================================================================
    // @After — runs AFTER every @waitlist scenario
    // ====================================================================

    @After(value = "@waitlist", order = 1)
    public void teardownWaitlistTestData(Scenario scenario) {
        logger.info("═══ @After [waitlist] — {} — {} ═══",
                scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: {}", scenario.getName());
        }

        // Cancel any waitlist entry so the next scenario starts clean
        // (DELETE /api/waitlist/cancel)
        if (testUserToken != null) {
            WaitlistApiClient.cancelEntry(testUserToken);
            logger.info("Waitlist entry cancelled (cleanup) ✓");
        } else {
            // Re-obtain token if it wasn't cached
            String token = IdentityApiClient.getToken(
                    TestDataConfig.TEST_USER_EMAIL,
                    TestDataConfig.TEST_USER_PASSWORD);
            if (token != null) {
                WaitlistApiClient.cancelEntry(token);
                testUserToken = token;
                logger.info("Waitlist entry cancelled (cleanup, fresh token) ✓");
            } else {
                logger.warn("Could not obtain token for cleanup — skipping waitlist cancel.");
            }
        }

        logger.info("═══ Teardown complete ═══");
    }

    // ====================================================================
    // Generic @Before/@After for ALL scenarios (lightweight)
    // ====================================================================

    @Before(order = 0)
    public void logScenarioStart(Scenario scenario) {
        logger.info("▶ Starting scenario: {} [tags: {}]",
                scenario.getName(), scenario.getSourceTagNames());
    }

    @After(order = 0)
    public void logScenarioEnd(Scenario scenario) {
        logger.info("◀ Finished scenario: {} — result: {}",
                scenario.getName(), scenario.getStatus());
    }
}
