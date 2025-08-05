package org.veeva.core.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"org.veeva.core.stepDefinitions", "org.veeva.utilities"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        tags="@Default"
        // DO NOT specify 'tags' here
)
public class RunCucumberTestCP extends AbstractTestNGCucumberTests {
        @BeforeClass(alwaysRun = true)
        @Parameters({"browser","cucumber.filter.tags"})
        public void setUpBrowser(String browser,String tags) {
                // Set the browser as a system property so DriverFactory picks it up
                System.setProperty("browser", browser);
                System.setProperty("cucumber.filter.tags", tags);
        }
}