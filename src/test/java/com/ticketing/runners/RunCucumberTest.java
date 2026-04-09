package com.ticketing.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.ticketing.stepdefinitions", "com.ticketing.hooks"},
        tags = "@smoke or @positive or @negative or @security",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/html/report.html",
                "json:target/cucumber-reports/json/report.json",
                "junit:target/cucumber-reports/junit/report.xml"
        }
)
public class RunCucumberTest {
}
