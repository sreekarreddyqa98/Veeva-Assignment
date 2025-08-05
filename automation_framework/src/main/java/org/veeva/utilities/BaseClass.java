/**
 * BaseClass - Abstract Foundation for Page Object Model Implementation
 * 
 * This abstract class serves as the foundation for all Page Object classes in the automation framework.
 * It implements the Page Object Model (POM) design pattern and provides common functionality that
 * all page objects need to interact with web elements and perform browser operations.
 * 
 * Key Features:
 * - Automatic PageFactory initialization for @FindBy annotations
 * - Pre-configured WebDriverWait for element synchronization
 * - Actions class for advanced user interactions (hover, drag-drop, etc.)
 * - Integrated utility classes for common operations
 * - Allure reporting integration for test documentation
 * 
 * Design Pattern: Abstract Factory Pattern
 * Usage: All page object classes must extend this base class
 * Thread Safety: Uses ThreadLocal WebDriver for parallel execution
 * 
 * @author Veeva Automation Team
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseClass {

    // Core WebDriver instance for browser interactions
    protected WebDriver driver;
    
    // WebDriverWait for explicit waits and element synchronization
    protected WebDriverWait wait;
    
    // Actions class for complex user interactions (hover, drag-drop, keyboard actions)
    protected Actions actions;
    
    // Utility class instance for common web operations
    protected Utilities utils;
    
    // Allure reporting utility for test documentation and screenshots
    protected AllureReportUtils allureUtil;

    /**
     * Constructor for BaseClass - Initializes all core components for page objects
     * 
     * This constructor performs the following initialization:
     * 1. Sets up the WebDriver instance
     * 2. Initializes PageFactory for @FindBy annotations
     * 3. Configures WebDriverWait with 10-second timeout
     * 4. Creates Actions instance for advanced interactions
     * 5. Initializes utility classes for common operations
     * 
     * @param driver WebDriver instance (Chrome, Firefox, Edge) from DriverFactory
     */
    public BaseClass(WebDriver driver) {
        // Store the WebDriver instance for use in page object methods
        this.driver = driver;
        
        // Initialize PageFactory to enable @FindBy annotations in child classes
        // This automatically initializes all WebElement fields annotated with @FindBy
        PageFactory.initElements(driver, this);
        
        // Set up WebDriverWait with 10-second timeout for explicit waits
        // This helps in waiting for elements to be visible, clickable, etc.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Initialize Actions class for complex user interactions
        // Used for hover, drag-drop, right-click, keyboard combinations
        this.actions = new Actions(driver);
        
        // Create utility class instance for common web operations
        // Provides reusable methods for scrolling, clicking, element operations
        this.utils = new Utilities(driver);
        
        // Initialize Allure reporting utility for test documentation
        // Enables screenshot capture, file attachments, and test reporting
        this.allureUtil = new AllureReportUtils();
    }
}

