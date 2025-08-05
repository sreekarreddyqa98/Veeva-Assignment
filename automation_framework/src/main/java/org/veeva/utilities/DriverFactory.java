/**
 * DriverFactory - WebDriver Factory with ThreadLocal Support for Parallel Execution
 * 
 * This class implements the Factory Design Pattern to create and manage WebDriver instances
 * across different browsers. It uses ThreadLocal storage to ensure thread-safe parallel
 * test execution, allowing multiple tests to run simultaneously without interference.
 * 
 * Key Features:
 * - ThreadLocal WebDriver management for parallel execution
 * - Multi-browser support (Chrome, Firefox, Edge)
 * - Headless execution for CI/CD environments
 * - Anti-detection configurations to avoid bot detection
 * - Custom browser options for optimal test execution
 * - Automatic resource cleanup to prevent memory leaks
 * 
 * Supported Browsers:
 * - Google Chrome (with anti-detection features)
 * - Mozilla Firefox (with custom preferences)
 * - Microsoft Edge (basic configuration)
 * 
 * Design Pattern: Factory Pattern + ThreadLocal Pattern
 * Thread Safety: Yes (ThreadLocal ensures each thread has its own WebDriver)
 * 
 * @author Veeva Automation Team
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    // ThreadLocal storage for WebDriver instances to support parallel execution
    // Each thread gets its own WebDriver instance, preventing interference between tests
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Initializes WebDriver based on browser configuration from config.properties
     * 
     * This method reads the browser type from configuration and creates the appropriate
     * WebDriver instance with optimized settings for test automation:
     * 
     * Chrome Configuration:
     * - Anti-detection features to avoid bot detection
     * - Headless mode for CI/CD environments
     * - Custom user agent to mimic real browser
     * - High resolution window size for element visibility
     * 
     * Firefox Configuration:
     * - Custom user agent preferences
     * - Window size optimization
     * - Headless mode for CI/CD environments
     * 
     * Thread Safety: Uses ThreadLocal to store WebDriver per thread
     */
    public static void initDriver() {
        try {
            // Read browser configuration from config.properties file
//            String browser = ConfigReader.getProperty("browser");
            String browser = System.getProperty("browser");

            // Initialize Chrome WebDriver with anti-detection and optimization settings
            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                
                // Anti-detection configurations to avoid being detected as automation
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.setExperimentalOption("useAutomationExtension", false);
                
                // Window and display configurations
                options.addArguments("--start-maximized");  // Start browser in maximized window
                options.addArguments("--window-size=3840,2160");  // Set high resolution for better element detection
                
                // Custom user agent to mimic real Chrome browser
                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
                
                // Headless mode for CI/CD environments (runs without GUI)
//                options.addArguments("--headless=new");  // New headless mode for better performance
//                options.addArguments("--disable-gpu");   // Disable GPU for headless mode stability

                // Create ChromeDriver instance and store in ThreadLocal
                driver.set(new ChromeDriver(options));
                
                // Set system property for reporting and logging
                System.setProperty("browser.name", browser);
            }

            // Initialize Firefox WebDriver with custom preferences
            if (browser.equalsIgnoreCase("firefox")) {
                // Optional: Set geckodriver path (not needed if using Selenium Manager)
//                System.setProperty("webdriver.firefox.driver", "path/to/geckodriver.exe");

                FirefoxOptions options = new FirefoxOptions();
                
                // Window size configurations for Firefox
                options.addArguments("--start-maximized");
                options.addArguments("--width=3840");
                options.addArguments("--height=2160");
                
//                // Headless mode for CI/CD environments
//                options.addArguments("--headless");     // Firefox headless mode
//                options.addArguments("--disable-gpu");  // GPU disable for Firefox stability
                
                // Set custom user agent preference for Firefox
                options.addPreference("general.useragent.override", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

                // Create FirefoxDriver instance and store in ThreadLocal
                driver.set(new FirefoxDriver(options));
                
                // Set system property for reporting and logging
                System.setProperty("browser.name", browser);
            }

        } catch (Exception e) {
            // Print stack trace for debugging WebDriver initialization issues
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the WebDriver instance for the current thread
     * 
     * This method returns the WebDriver instance stored in ThreadLocal for the current
     * executing thread. This ensures that each test thread gets its own WebDriver instance
     * for parallel execution without interference.
     * 
     * @return WebDriver instance for the current thread
     * @throws NullPointerException if WebDriver is not initialized for current thread
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Safely quits the WebDriver and cleans up ThreadLocal storage
     * 
     * This method performs proper cleanup of WebDriver resources:
     * 1. Checks if WebDriver instance exists for current thread
     * 2. Calls quit() to close all browser windows and end WebDriver session
     * 3. Removes the WebDriver instance from ThreadLocal to prevent memory leaks
     * 
     * Should be called in test teardown methods or Cucumber @After hooks
     * to ensure proper resource cleanup and prevent memory leaks.
     */
    public static void quitDriver() {
        // Check if WebDriver instance exists for current thread
        if (driver.get() != null) {
            // Close all browser windows and end the WebDriver session
            driver.get().quit();
            
            // Remove WebDriver instance from ThreadLocal to prevent memory leaks
            driver.remove();
        }
    }
}
