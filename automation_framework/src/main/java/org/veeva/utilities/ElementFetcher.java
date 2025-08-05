/**
 * ElementFetcher - Dynamic Web Element Retrieval Using Reflection
 * 
 * This utility class provides dynamic access to WebElement fields in Page Object classes
 * using Java Reflection API. It enables runtime retrieval of elements by field name,
 * making the framework more flexible and reducing code duplication.
 * 
 * Key Features:
 * - Dynamic element retrieval by field name using reflection
 * - Automatic visibility wait for retrieved elements
 * - Support for both single WebElement and List<WebElement> fields
 * - Generic return type for type-safe element access
 * - Comprehensive error handling for reflection operations
 * 
 * Use Cases:
 * - Dynamic element access in step definitions
 * - Generic utility methods that work with any page object
 * - Data-driven testing where element names come from external sources
 * - Reducing code duplication in page object interactions
 * 
 * Design Pattern: Reflection Pattern + Template Method Pattern
 * Inheritance: Extends BaseClass for WebDriver and utility access
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ElementFetcher extends BaseClass {

    // WebDriver instance for this ElementFetcher (inherited from BaseClass)
    private WebDriver driver;

    /**
     * Constructor for ElementFetcher
     * 
     * Initializes the ElementFetcher by calling the parent BaseClass constructor,
     * which sets up WebDriver, WebDriverWait, Actions, and other utilities
     * needed for element operations.
     * 
     * @param driver WebDriver instance from DriverFactory
     */
    public ElementFetcher(WebDriver driver) {
        // Call parent constructor to initialize WebDriver and utilities
        super(driver);
    }

    /**
     * Dynamically retrieves a WebElement or List<WebElement> by field name using reflection
     * 
     * This method uses Java Reflection to access private/protected fields in Page Object
     * classes by their field names. It automatically waits for element visibility
     * before returning the element, ensuring the element is ready for interaction.
     * 
     * Supported Field Types:
     * - WebElement: Single web element (waits for visibility)
     * - List<WebElement>: List of web elements (waits for first element visibility)
     * 
     * Process Flow:
     * 1. Use reflection to access the specified field in the page object
     * 2. Make the field accessible (bypass private/protected modifiers)
     * 3. Get the field value (WebElement or List<WebElement>)
     * 4. Apply appropriate wait condition based on field type
     * 5. Return the element with proper type casting
     * 
     * @param <T> Generic type parameter for return type (WebElement or List<WebElement>)
     * @param pageObject The page object instance containing the field
     * @param fieldName The name of the field to retrieve (must match exact field name)
     * @return The WebElement or List<WebElement> with proper type casting
     * 
     * @throws RuntimeException if field doesn't exist, is inaccessible, or wait times out
     * 
     * Usage Examples:
     * - WebElement loginButton = fetcher.getElementByFieldName(loginPage, "loginButton");
     * - List<WebElement> menuItems = fetcher.getElementByFieldName(homePage, "menuItems");
     */
    @SuppressWarnings("unchecked")
    public <T> T getElementByFieldName(Object pageObject, String fieldName) {
        try {
            // Use reflection to get the field from the page object class
            // getDeclaredField() gets fields declared in this class (including private)
            Field field = pageObject.getClass().getDeclaredField(fieldName);
            
            // Make the field accessible, bypassing private/protected access modifiers
            // This allows us to access private WebElement fields in page objects
            field.setAccessible(true);
            
            // Get the actual value of the field from the page object instance
            // This returns the WebElement or List<WebElement> object
            Object value = field.get(pageObject);

            // Handle single WebElement fields
            if (value instanceof WebElement) {
                // Wait for the WebElement to be visible before returning
                // This ensures the element is ready for interaction
                wait.until(ExpectedConditions.visibilityOf((WebElement) value));
                
            // Handle List<WebElement> fields
            } else if (value instanceof List && !((List<?>) value).isEmpty()) {
                // Get the first element from the list to check visibility
                Object first = ((List<?>) value).getFirst();
                
                // If the first element is a WebElement, wait for its visibility
                // This ensures at least one element in the list is visible
                if (first instanceof WebElement) {
                    wait.until(ExpectedConditions.visibilityOf((WebElement) first));
                }
            }

            // Return the value with proper generic type casting
            // The @SuppressWarnings("unchecked") annotation handles the casting warning
            return (T) value;
            
        } catch (Exception e) {
            // Handle any exceptions during reflection or element operations
            throw new RuntimeException(
                "Unable to fetch field '" + fieldName + "' from " + 
                pageObject.getClass().getSimpleName() + ": " + e.getMessage(), e
            );
        }
    }
}
