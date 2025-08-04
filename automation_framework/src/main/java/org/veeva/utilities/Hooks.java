//package org.veeva.utilities;
//
//import io.cucumber.java.After;
//import io.cucumber.java.AfterStep;
//import io.cucumber.java.Before;
//import io.cucumber.java.Scenario;
//import io.qameta.allure.Allure;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import java.io.ByteArrayInputStream;
//import java.time.Duration;
//
//public class Hooks {
//
//    public static WebDriver driver;
//    public static WebDriverWait wait;
//
//    @Before
//    public void setUp() {
//        driver = DriverFactory.initDriver();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
//
//    @After
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//
//    @AfterStep
//    public void captureScreenshot(Scenario scenario) {
//        if (scenario.isFailed()) {
//            WebDriver driver = DriverFactory.getDriver();
//            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//            Allure.addAttachment("Failed Step Screenshot", new ByteArrayInputStream(screenshot));
//        }
//    }
//
//
//    public static WebDriver getDriver() {
//        return driver;
//    }
//
//    public static WebDriverWait getWait() {
//        return wait;
//    }
//}

package org.veeva.utilities;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class Hooks {

    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    @Before
    public void setUp() {
        DriverFactory.initDriver();
        WebDriver driver = DriverFactory.getDriver();
        wait.set(new WebDriverWait(driver, Duration.ofSeconds(10)));
        Allure.label("browser", ConfigReader.getProperty("browser"));
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
        wait.remove();
    }

    @AfterStep
    public void captureScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failed Step Screenshot", new ByteArrayInputStream(screenshot));
        }
    }

    public static WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public static WebDriverWait getWait() {
        return wait.get();
    }
}
