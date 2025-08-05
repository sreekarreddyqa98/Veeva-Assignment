/**
 * AllureReportUtils - Utility Class for Allure Test Reporting Integration
 * 
 * This utility class provides comprehensive methods for integrating test artifacts
 * with Allure reporting framework. It enables attachment of various file types,
 * text content, and screenshots to test reports for better documentation and debugging.
 * 
 * Key Features:
 * - File attachment support for multiple MIME types (CSV, TXT, JSON, etc.)
 * - Text content attachment for logs and debug information
 * - Screenshot attachment for visual test evidence
 * - Automatic file extension detection for proper rendering
 * - Comprehensive error handling for missing or corrupted files
 * 
 * Supported Attachments:
 * - CSV files (test data, results)
 * - Text files (logs, extracted data)
 * - Screenshots (PNG format)
 * - JSON files (API responses, configuration)
 * - Any file type with proper MIME type specification
 * 
 * Usage in Tests:
 * - Attach test data files for reference
 * - Include screenshots of test execution
 * - Add debug logs and error messages
 * - Document test artifacts and evidence
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureReportUtils {

    /**
     * Attaches a file to the Allure report with specified name and MIME type
     * 
     * This method reads a file from the filesystem and attaches it to the current
     * Allure test report. The file is displayed in the Allure report with the
     * specified name and rendered according to its MIME type.
     * 
     * Common MIME types:
     * - "text/csv" for CSV files
     * - "text/plain" for text files
     * - "application/json" for JSON files
     * - "image/png" for PNG screenshots
     * 
     * @param name Display name for the attachment in Allure report
     * @param filePath Absolute or relative path to the file to attach
     * @param mimeType MIME type of the file for proper rendering
     */
    public static void attachFileToAllure(String name, String filePath, String mimeType) {
        try {
            // Create File object from the provided file path
            File file = new File(filePath);
            
            // Check if file exists before attempting to read
            if (file.exists()) {
                // Read all bytes from the file into memory
                byte[] fileContent = Files.readAllBytes(file.toPath());
                
                // Attach file content to Allure report with proper extension
                // The file extension helps Allure render the content correctly
                Allure.addAttachment(name, mimeType, new ByteArrayInputStream(fileContent), getFileExtension(file));
            } else {
                // Log error if file doesn't exist
                System.err.println("File not found for Allure attachment: " + filePath);
            }
        } catch (IOException e) {
            // Handle IO exceptions during file reading
            System.err.println("Failed to attach file to Allure report: " + e.getMessage());
        }
    }

    /**
     * Attaches text content to the Allure report
     * 
     * This method attaches plain text content directly to the Allure report
     * without requiring a physical file. Useful for attaching logs, debug
     * information, error messages, or any string-based content.
     * 
     * Common use cases:
     * - Exception stack traces
     * - Debug logs and messages
     * - Test data in string format
     * - API response content
     * - Configuration information
     * 
     * @param name Display name for the text attachment in Allure report
     * @param content Text content to attach (can be multi-line)
     */
    public static void attachTextToAllure(String name, String content) {
        // Attach text content with plain text MIME type
        // Allure will display this as formatted text in the report
        Allure.addAttachment(name, "text/plain", content);
    }

    /**
     * Attaches screenshot bytes to the Allure report
     * 
     * This method attaches screenshot data (as byte array) to the Allure report.
     * Typically used for attaching screenshots captured during test execution,
     * especially for failed test steps or important verification points.
     * 
     * The screenshot is attached as PNG format and displayed as an image
     * in the Allure report for visual verification.
     * 
     * @param name Display name for the screenshot in Allure report
     * @param screenshotBytes Screenshot data as byte array (PNG format)
     */
    public static void attachScreenshotBytes(String name, byte[] screenshotBytes) {
        // Attach screenshot bytes with PNG MIME type and extension
        // Allure will render this as an image in the report
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshotBytes), "png");
    }

    /**
     * Extracts file extension from a File object
     * 
     * This private helper method extracts the file extension from a File object
     * to help Allure properly render the attached file. The extension is used
     * by Allure to determine how to display the file content.
     * 
     * Examples:
     * - "data.csv" returns "csv"
     * - "screenshot.png" returns "png"
     * - "log.txt" returns "txt"
     * - "file" returns "" (empty string for files without extension)
     * 
     * @param file File object to extract extension from
     * @return File extension without the dot, or empty string if no extension
     */
    private static String getFileExtension(File file) {
        // Get the file name from the File object
        String name = file.getName();
        
        // Find the last occurrence of dot (.) in the filename
        int lastDot = name.lastIndexOf(".");
        
        // Return extension without dot, or empty string if no dot found
        return (lastDot == -1) ? "" : name.substring(lastDot + 1);
    }
}
