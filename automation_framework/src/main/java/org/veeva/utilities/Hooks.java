/**
 * Hooks - Cucumber Lifecycle Management and Test Setup/Teardown
 * 
 * This class manages the Cucumber test lifecycle by providing setup and teardown hooks
 * that run before and after each test scenario. It handles WebDriver initialization,
 * cleanup, and automatic screenshot capture for failed test steps.
 * 
 * Key Features:
 * - Automatic WebDriver initialization before each scenario
 * - Proper resource cleanup after each scenario
 * - Automatic screenshot capture on test failures
 * - Allure reporting integration with browser labeling
 * - ThreadLocal WebDriverWait management for parallel execution
 * 
 * Cucumber Hooks:
 * - @Before: Runs before each scenario starts
 * - @After: Runs after each scenario completes
 * - @AfterStep: Runs after each step execution
 * 
 * Thread Safety: Uses ThreadLocal for WebDriverWait to support parallel execution
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
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

    // ThreadLocal WebDriverWait for parallel execution support
    // Each thread gets its own WebDriverWait instance
    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    /**
     * Setup method that runs before each Cucumber scenario
     * 
     * This method performs the following setup operations:
     * 1. Initializes WebDriver through DriverFactory
     * 2. Creates WebDriverWait instance for the current thread
     * 3. Sets up Allure reporting with browser information
     * 
     * The @Before annotation ensures this method runs before every scenario,
     * providing a fresh browser instance for each test.
     */
    @Before
    public void setUp() {
        // Initialize WebDriver based on configuration (Chrome, Firefox, etc.)
        DriverFactory.initDriver();
        
        // Get the WebDriver instance for current thread
        WebDriver driver = DriverFactory.getDriver();
        
        // Create WebDriverWait with 10-second timeout and store in ThreadLocal
        // This allows each thread to have its own wait instance for parallel execution
        wait.set(new WebDriverWait(driver, Duration.ofSeconds(10)));
        
        // Add browser information to Allure report for better test documentation
        // This helps identify which browser was used for each test execution
        Allure.label("browser", ConfigReader.getProperty("browser"));
    }

    /**
     * Teardown method that runs after each Cucumber scenario
     * 
     * This method performs cleanup operations to ensure proper resource management:
     * 1. Quits the WebDriver instance and closes all browser windows
     * 2. Removes WebDriverWait from ThreadLocal to prevent memory leaks
     * 
     * The @After annotation ensures this method runs after every scenario,
     * regardless of whether the test passed or failed.
     */
    @After
    public void tearDown() {
        // Quit WebDriver and clean up ThreadLocal storage
        // This closes all browser windows and ends the WebDriver session
        DriverFactory.quitDriver();
        
        // Remove WebDriverWait from ThreadLocal to prevent memory leaks
        // This is important for long-running test suites
        wait.remove();
    }

    /**
     * Screenshot capture method that runs after each Cucumber step
     * 
     * This method automatically captures screenshots when a test step fails,
     * providing visual evidence of the failure state for debugging purposes.
     * The screenshot is attached to the Allure report for easy access.
     * 
     * @param scenario The current Cucumber scenario being executed
     */
    @AfterStep
    public void captureScreenshot(Scenario scenario) {
        // Check if the current scenario has failed
        if (scenario.isFailed()) {
            // Capture screenshot as byte array from the current WebDriver instance
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            
            // Attach the screenshot to Allure report with descriptive name
            // This helps in debugging by showing the exact state when the test failed
            Allure.addAttachment("Failed Step Screenshot", new ByteArrayInputStream(screenshot));
        }
    }

    /**
     * Static method to get WebDriver instance for the current thread
     * 
     * This is a convenience method that delegates to DriverFactory.getDriver()
     * and can be used by other classes that need access to the WebDriver instance.
     * 
     * @return WebDriver instance for the current thread
     */
    public static WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    /**
     * Static method to get WebDriverWait instance for the current thread
     * 
     * This method returns the WebDriverWait instance stored in ThreadLocal
     * for the current thread, enabling explicit waits in test steps.
     * 
     * @return WebDriverWait instance for the current thread
     */
    public static WebDriverWait getWait() {
        return wait.get();
    }
}
