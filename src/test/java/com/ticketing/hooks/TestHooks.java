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

public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);

    private static volatile boolean dataSeeded = false;
    private static volatile String testUserToken = null;

    @Before(value = "@waitlist", order = 1)
    public void setupWaitlistTestData(Scenario scenario) {
        logger.info("═══ @Before [waitlist] — {} ═══", scenario.getName());

        if (dataSeeded) {
            logger.info("Test data already seeded. Skipping API calls.");
            return;
        }

        if (!IdentityApiClient.isHealthy()) {
            logger.error("Identity Service unreachable at {}", TestDataConfig.IDENTITY_BASE_URL);
            throw new IllegalStateException(
                "Identity Service unreachable: " + TestDataConfig.IDENTITY_BASE_URL);
        }
        logger.info("Identity Service healthy ✓");

        boolean userCreated = IdentityApiClient.createUser(
            TestDataConfig.TEST_USER_EMAIL,
            TestDataConfig.TEST_USER_PASSWORD,
            TestDataConfig.TEST_USER_ROLE);
        if (!userCreated) {
            throw new IllegalStateException(
                "Could not create test user: " + TestDataConfig.TEST_USER_EMAIL);
        }
        logger.info("Test user ready: {} ✓", TestDataConfig.TEST_USER_EMAIL);

        testUserToken = IdentityApiClient.getToken(
            TestDataConfig.TEST_USER_EMAIL,
            TestDataConfig.TEST_USER_PASSWORD);
        if (testUserToken != null) {
            logger.info("Test user token cached for cleanup ✓");
        }

        boolean reserveUserCreated = IdentityApiClient.createUser(
            TestDataConfig.RESERVE_USER_EMAIL,
            TestDataConfig.RESERVE_USER_PASSWORD,
            TestDataConfig.RESERVE_USER_ROLE);
        if (!reserveUserCreated) {
            throw new IllegalStateException(
                "Could not create reserve user: " + TestDataConfig.RESERVE_USER_EMAIL);
        }
        logger.info("Reserve user ready: {} ✓", TestDataConfig.RESERVE_USER_EMAIL);

        IdentityApiClient.createUser(
            TestDataConfig.ADMIN_EMAIL,
            TestDataConfig.ADMIN_PASSWORD,
            TestDataConfig.ADMIN_ROLE);
        logger.info("Admin user ready: {} ✓", TestDataConfig.ADMIN_EMAIL);

        String adminToken = IdentityApiClient.getToken(
            TestDataConfig.ADMIN_EMAIL,
            TestDataConfig.ADMIN_PASSWORD);
        if (adminToken != null) {
            EventApiClient.setAdminToken(adminToken);
            logger.info("Admin token set for EventApiClient ✓");
        } else {
            logger.warn("Could not obtain admin token — event creation may fail.");
        }

        dataSeeded = true;
        logger.info("═══ Data seeding complete ═══");
    }

    @After(value = "@waitlist", order = 1)
    public void teardownWaitlistTestData(Scenario scenario) {
        logger.info("═══ @After [waitlist] — {} — {} ═══",
                scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: {}", scenario.getName());
        }

        if (testUserToken != null) {
            WaitlistApiClient.cancelEntry(testUserToken);
            logger.info("Waitlist entry cancelled (cleanup) ✓");
        } else {
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
