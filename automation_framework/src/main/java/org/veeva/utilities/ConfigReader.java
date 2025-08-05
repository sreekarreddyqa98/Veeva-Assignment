/**
 * ConfigReader - Configuration Properties File Reader
 * 
 * This utility class implements the Singleton pattern to read and manage configuration
 * properties from the config.properties file. It provides centralized access to all
 * configuration values used throughout the automation framework.
 * 
 * Key Features:
 * - Singleton pattern ensures single instance of Properties object
 * - Static initialization block loads properties at class loading time
 * - Thread-safe access to configuration properties
 * - Comprehensive error handling for missing or corrupted config files
 * - Centralized configuration management for the entire framework
 * 
 * Configuration Properties Include:
 * - browser: Browser type (chrome, firefox, edge)
 * - CP_Home_Page: Core Product home page URL
 * - DP1_Home_Page: Derived Product 1 home page URL
 * - DP2_Home_Page: Derived Product 2 home page URL
 * 
 * Design Pattern: Singleton Pattern
 * Thread Safety: Yes (Properties object is thread-safe for read operations)
 * 
 * @author Veeva Automation Team
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    // Static Properties object to hold all configuration key-value pairs
    // Using static ensures single instance across the entire application
    private static final Properties prop = new Properties();

    // Static initialization block - runs when class is first loaded
    // This ensures configuration is loaded once and available throughout application lifecycle
    static {
        try (
            // Use try-with-resources to automatically close InputStream
            // Get config.properties file from classpath resources/configuration folder
            InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("configuration/config.properties")
        ) {
            // Check if config.properties file exists in the specified location
            if (input == null) {
                throw new RuntimeException(
                    "config.properties file not found in resources/configuration folder. "
                    + "Please ensure the file exists at: src/main/resources/configuration/config.properties"
                );
            }

            // Load all properties from the input stream into Properties object
            // This reads all key=value pairs from the config.properties file
            prop.load(input);
            
            // Log successful configuration loading (optional)
            System.out.println("Configuration properties loaded successfully from config.properties");

        } catch (IOException e) {
            // Handle any IO exceptions that occur during file reading
            throw new RuntimeException(
                "Failed to load configuration file. Please check if config.properties exists and is readable.", 
                e
            );
        }
    }

    /**
     * Retrieves a configuration property value by its key
     * 
     * This method provides thread-safe access to configuration properties.
     * It returns the value associated with the specified key from the
     * config.properties file.
     * 
     * Common usage examples:
     * - ConfigReader.getProperty("browser") -> returns "chrome"
     * - ConfigReader.getProperty("CP_Home_Page") -> returns NBA Warriors URL
     * - ConfigReader.getProperty("DP1_Home_Page") -> returns NBA Sixers URL
     * 
     * @param key The property key to look up (e.g., "browser", "CP_Home_Page")
     * @return The property value as String, or null if key doesn't exist
     * 
     * @throws IllegalArgumentException if key is null or empty
     */
    public static String getProperty(String key) {
        // Validate input parameter
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Property key cannot be null or empty");
        }
        
        // Retrieve property value from Properties object
        // Properties.getProperty() is thread-safe for read operations
        String value = prop.getProperty(key);
        
        // Log warning if property key is not found (optional for debugging)
        if (value == null) {
            System.err.println("Warning: Property key '" + key + "' not found in config.properties");
        }
        
        return value;
    }
    
    /**
     * Retrieves a configuration property with a default value
     * 
     * This overloaded method provides a fallback value if the specified
     * key is not found in the configuration file.
     * 
     * @param key The property key to look up
     * @param defaultValue The default value to return if key is not found
     * @return The property value if found, otherwise the default value
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null) ? value : defaultValue;
    }
}
