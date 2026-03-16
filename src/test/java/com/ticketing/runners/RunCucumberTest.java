package com.ticketing.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Cucumber Test Runner for Serenity BDD
 * This class configures and executes all Cucumber/Gherkin feature tests
 * Using traditional Serenity runner for better compatibility
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.ticketing.stepdefinitions",
        tags = "@smoke or @positive or @negative or @security",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/html/report.html",
                "json:target/cucumber-reports/json/report.json",
                "junit:target/cucumber-reports/junit/report.xml"
        }
)
public class RunCucumberTest {
    // This class remains empty, it's just a runner configuration
    
    /*
     * Test Execution Notes:
     * 
     * 1. Run all tests:
     *    ./gradlew test
     * 
     * 2. Run specific tags:
     *    ./gradlew test -Dcucumber.filter.tags="@smoke"
     * 
     * 3. Run specific feature:
     *    ./gradlew test -Dcucumber.features="src/test/resources/features/events.feature"
     * 
     * 4. Generate Serenity reports:
     *    ./gradlew test aggregate
     * 
     * 5. Run in different browsers:
     *    ./gradlew test -Dwebdriver.driver=firefox
     *    ./gradlew test -Dwebdriver.driver=chrome
     * 
     * 6. Run headless:
     *    ./gradlew test -Dwebdriver.chrome.driver.headless=true
     * 
     * 7. Parallel execution:
     *    Available through serenity.conf parallel.tests setting
     */
}