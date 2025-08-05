/**
 * RunnerGenerator - Dynamic Cucumber Test Runner Class Generator
 * 
 * This utility class automatically generates Cucumber TestNG runner classes
 * based on existing feature files. It scans the features directory and creates
 * corresponding runner classes with proper annotations and configurations.
 * 
 * Key Features:
 * - Automatic runner class generation from feature files
 * - TestNG-Cucumber integration with proper annotations
 * - Configurable features and glue package paths
 * - HTML report generation for each runner
 * - Directory structure creation and management
 * 
 * Generated Runner Features:
 * - Extends AbstractTestNGCucumberTests for TestNG integration
 * - @CucumberOptions annotation with proper configuration
 * - Individual HTML reports for each feature
 * - Monochrome output for better readability
 * 
 * Use Cases:
 * - Rapid test runner creation for new features
 * - Maintaining consistent runner configurations
 * - Automating test setup for large feature sets
 * - CI/CD pipeline integration for dynamic test execution
 * 
 * Directory Structure:
 * - Input: src/test/resources/features/*.feature
 * - Output: src/test/java/org/veeva/runner/*Runner.java
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunnerGenerator {

    // Directory path for Cucumber feature files
    private static final String FEATURES_DIR = "src/test/resources/features";
    
    // Directory path for generated runner classes
    private static final String RUNNERS_DIR = "src/test/java/org/veeva/runner";

    /**
     * Main method to generate runner classes from feature files
     * 
     * This method scans the features directory for .feature files and generates
     * corresponding TestNG-Cucumber runner classes. Each runner is configured
     * to execute a specific feature file with proper reporting.
     * 
     * Process Flow:
     * 1. Scan features directory for .feature files
     * 2. Create runners directory if it doesn't exist
     * 3. Generate runner class for each feature file
     * 4. Write runner classes to appropriate directory
     * 
     * @param args Command line arguments (not used)
     * @throws IOException if file operations fail
     */
    public static void main(String[] args) throws IOException {
        // Create File object for features directory
        File featureDir = new File(FEATURES_DIR);
        
        // Get all .feature files from the features directory
        // Using lambda expression to filter files by .feature extension
        File[] featureFiles = featureDir.listFiles((dir, name) -> name.endsWith(".feature"));

        // Exit if no feature files found or directory doesn't exist
        if (featureFiles == null) return;

        // Create runners directory if it doesn't exist
        // mkdirs() creates parent directories as needed
        new File(RUNNERS_DIR).mkdirs();

        // Generate runner class for each feature file
        for (File feature : featureFiles) {
            // Extract feature name without .feature extension
            String featureName = feature.getName().replace(".feature", "");
            
            // Generate runner class name by capitalizing feature name and adding "Runner"
            String className = capitalize(featureName) + "Runner";
            
            // Generate the complete Java class content
            String classContent = generateRunnerClass(className, feature.getName());

            // Create Java file for the runner class
            File javaFile = new File(RUNNERS_DIR + "/" + className + ".java");
            
            // Write class content to file using try-with-resources for automatic cleanup
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
                writer.write(classContent);
            }
        }
    }

    /**
     * Generates the complete Java class content for a Cucumber runner
     * 
     * This method creates a properly formatted Java class that extends
     * AbstractTestNGCucumberTests and includes all necessary annotations
     * and configurations for Cucumber-TestNG integration.
     * 
     * Generated Class Features:
     * - Package declaration for proper organization
     * - Required imports for Cucumber-TestNG integration
     * - @CucumberOptions annotation with comprehensive configuration
     * - Class declaration extending AbstractTestNGCucumberTests
     * 
     * @param className Name of the runner class to generate
     * @param featureFileName Name of the feature file to associate with runner
     * @return Complete Java class content as String
     */
    private static String generateRunnerClass(String className, String featureFileName) {
        return "package org.veeva.runner;\n\n" +
                "import io.cucumber.testng.AbstractTestNGCucumberTests;\n" +
                "import io.cucumber.testng.CucumberOptions;\n\n" +
                "@CucumberOptions(\n" +
                "    features = \"src/test/resources/features/" + featureFileName + "\",\n" +
                "    glue = \"org.veeva.stepDefinitions\",\n" +
                "    plugin = {\"pretty\", \"html:target/" + className + "-report.html\"},\n" +
                "    monochrome = true\n" +
                ")\n" +
                "public class " + className + " extends AbstractTestNGCucumberTests {\n" +
                "}";
    }

    /**
     * Capitalizes the first letter of a string
     * 
     * This utility method converts the first character of a string to uppercase
     * while keeping the rest of the string unchanged. Used for generating
     * proper Java class names from feature file names.
     * 
     * Examples:
     * - "login" becomes "Login"
     * - "userRegistration" becomes "UserRegistration"
     * - "API_Test" becomes "API_Test"
     * 
     * @param str String to capitalize
     * @return String with first character capitalized
     */
    private static String capitalize(String str) {
        // Capitalize first character and concatenate with rest of string
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
