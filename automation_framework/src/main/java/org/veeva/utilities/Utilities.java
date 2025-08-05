/**
 * Utilities - Common Web Automation Utility Methods
 * 
 * This utility class provides a comprehensive set of reusable methods for common
 * web automation tasks. It encapsulates frequently used operations like element
 * interactions, page navigation, scrolling, and browser tab management.
 * 
 * Key Features:
 * - Element interaction utilities (hover, click, scroll)
 * - Browser tab and window management
 * - Element counting and validation
 * - XPath extraction from @FindBy annotations
 * - Page object management and navigation
 * - Robust error handling with retry mechanisms
 * 
 * Common Use Cases:
 * - Hover over menu items to reveal dropdowns
 * - Safe clicking with visibility checks
 * - Scrolling to page sections or end of page
 * - Switching between browser tabs
 * - Counting elements for validation
 * - Extracting locator information for debugging
 * 
 * Design Pattern: Utility Pattern + Helper Methods
 * Thread Safety: Instance-based, each instance has its own WebDriver
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    // Current active page object for context-aware operations
    protected Object currentPage;
    
    // Map to store page objects for easy retrieval and management
    protected Map<String, Object> pageObjectMap = new HashMap<>();
    
    // Core WebDriver instance for browser interactions
    protected WebDriver driver;
    
    // WebDriverWait for explicit waits and synchronization
    protected WebDriverWait wait;
    
    // Actions class for advanced user interactions
    protected Actions actions;

    /**
     * Constructor for Utilities class
     * 
     * Initializes the utility class with WebDriver instance and sets up
     * necessary components for web automation operations.
     * 
     * @param driver WebDriver instance from DriverFactory
     */
    public Utilities(WebDriver driver) {
        // Store WebDriver instance for utility operations
        this.driver = driver;
        
        // Initialize WebDriverWait with 10-second timeout
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Initialize Actions class for complex interactions
        this.actions = new Actions(driver);
    }

    /**
     * Hovers over a WebElement with retry mechanism
     * 
     * This method performs a hover action on the specified element with built-in
     * error handling and retry logic. It waits for element visibility before
     * hovering and includes a brief pause after hover for UI stabilization.
     * 
     * Common use cases:
     * - Revealing dropdown menus
     * - Triggering tooltip displays
     * - Activating hover-based UI elements
     * 
     * @param element WebElement to hover over
     * @throws RuntimeException if hover operation fails after retry
     */
    public void hoverToElement(WebElement element) {
        try {
            // Wait for element to be visible before hovering
            wait.until(ExpectedConditions.visibilityOf(element));
            
            // Perform hover action using Actions class
            actions.moveToElement(element).perform();
            
            // Brief pause to allow UI to respond to hover
            Thread.sleep(Duration.ofMillis(1000));

        } catch (StaleElementReferenceException e) {
            // Retry once if element becomes stale (DOM refresh)
            // This handles cases where page content changes during hover
            hoverToElement(element);
            
        } catch (TimeoutException e) {
            // Handle timeout if element doesn't become visible
            throw new RuntimeException("Timed out waiting for hover element to be visible", e);
            
        } catch (Exception e) {
            // Handle any other unexpected errors during hover
            throw new RuntimeException("Unexpected error during hover operation", e);
        }
    }

    /**
     * Safely clicks an element if it's visible
     * 
     * This method performs a safe click operation by first waiting for element
     * visibility and then clicking. If any exception occurs, it's logged to
     * Allure report for debugging purposes.
     * 
     * @param element WebElement to click
     */
    public void clickIfVisible(WebElement element) {
        try {
            // Wait for element to be visible before clicking
            wait.until(ExpectedConditions.visibilityOf(element));
            
            // Perform click action
            element.click();
            
        } catch (Exception caughtException) {
            // Log exception details to Allure report for debugging
            // This helps identify issues without failing the test immediately
            AllureReportUtils.attachTextToAllure(
                "Click Exception", 
                "Exception occurred while clicking element: " + caughtException.getMessage()
            );
        }
    }

    /**
     * Scrolls to the end of the current page
     * 
     * This method uses JavaScript execution to scroll to the bottom of the page.
     * Useful for loading dynamic content, accessing footer elements, or
     * ensuring all page content is loaded.
     */
    public void scrollToPageEnd() {
        // Cast WebDriver to JavascriptExecutor for JavaScript execution
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Execute JavaScript to scroll to bottom of page
        // document.body.scrollHeight gets the total height of the page content
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Gets the count of elements in a WebElement list
     * 
     * This utility method returns the size of a WebElement list, commonly
     * used for validation and assertion purposes in tests.
     * 
     * @param elementList List of WebElements to count
     * @return Integer count of elements in the list
     */
    public int getCountOfElements(List<WebElement> elementList) {
        // Return the size of the element list
        // Handles null lists gracefully
        return elementList != null ? elementList.size() : 0;
    }

    /**
     * Switches to a browser tab/window with the specified title
     * 
     * This method iterates through all open browser windows/tabs and switches
     * to the one whose title contains the expected text. It refreshes each
     * tab to ensure the title is current.
     * 
     * @param expectedTitle Partial or complete title to search for
     * @throws RuntimeException if no tab with matching title is found
     */
    public void switchToTabWithTitle(String expectedTitle) {
        // Store the original window handle for fallback
        String originalHandle = driver.getWindowHandle();
        
        // Iterate through all available window handles
        for (String handle : driver.getWindowHandles()) {
            // Switch to the current window/tab
            driver.switchTo().window(handle);
            
            // Refresh the page to ensure title is current
            driver.navigate().refresh();
            
            try {
                // Check if current tab title contains expected text
                if (wait.until(ExpectedConditions.titleContains(expectedTitle))) {
                    // Found matching tab, stay on this tab and return
                    return;
                }
            } catch (TimeoutException ignored) {
                // Title doesn't match, continue to next tab
                // TimeoutException is expected for non-matching tabs
            }
        }
        
        // If no matching tab found, return to original tab
        driver.switchTo().window(originalHandle);
        
        // Throw exception indicating no matching tab was found
        throw new RuntimeException(
            "No browser tab found with title containing: '" + expectedTitle + "'"
        );
    }

    /**
     * Updates the current page object based on browser title
     * 
     * This private method maintains the current page context by matching
     * browser title with registered page objects.
     * 
     * @param title Current browser title
     */
    private void updateCurrentPageByTitle(String title) {
        // Iterate through registered page objects
        for (Map.Entry<String, Object> entry : pageObjectMap.entrySet()) {
            Object pageObj = entry.getValue();
            
            // Get expected title from page object
            String pageTitle = getPageTitleFromPageObject(pageObj);
            
            // If titles match, update current page reference
            if (pageTitle != null && title.contains(pageTitle)) {
                currentPage = pageObj;
                return;
            }
        }
    }

    /**
     * Extracts page title from page object using reflection
     * 
     * This private method uses reflection to call getPageTitle() method
     * on page objects to retrieve their expected titles.
     * 
     * @param pageObj Page object instance
     * @return Expected page title or null if method doesn't exist
     */
    private String getPageTitleFromPageObject(Object pageObj) {
        try {
            // Use reflection to find getPageTitle method
            Method method = pageObj.getClass().getMethod("getPageTitle");
            
            // Invoke the method and return the title
            return (String) method.invoke(pageObj);
            
        } catch (Exception e) {
            // Return null if method doesn't exist or invocation fails
            return null;
        }
    }

    /**
     * Extracts XPath value from @FindBy annotation using reflection
     * 
     * This static utility method uses reflection to extract XPath locator
     * values from @FindBy annotations on WebElement fields. Useful for
     * debugging and dynamic locator operations.
     * 
     * @param clazz Class containing the field with @FindBy annotation
     * @param fieldName Name of the field to extract XPath from
     * @return XPath string from @FindBy annotation
     * @throws RuntimeException if field doesn't exist or lacks @FindBy(xpath)
     */
    public static String getElementXpathValue(Class<?> clazz, String fieldName) {
        try {
            // Get the field using reflection
            Field field = clazz.getDeclaredField(fieldName);
            
            // Make field accessible for annotation reading
            field.setAccessible(true);
            
            // Get @FindBy annotation from the field
            FindBy findBy = field.getAnnotation(FindBy.class);
            
            // Check if annotation exists and has xpath value
            if (findBy != null && !findBy.xpath().isEmpty()) {
                return findBy.xpath();
            }
            
            // Throw exception if field lacks proper @FindBy(xpath) annotation
            throw new RuntimeException(
                "Field '" + fieldName + "' is not annotated with @FindBy(xpath) or xpath is empty"
            );
            
        } catch (Exception e) {
            // Handle any reflection-related errors
            throw new RuntimeException(
                "Error accessing field '" + fieldName + "' in class " + clazz.getSimpleName(), e
            );
        }
    }
}
