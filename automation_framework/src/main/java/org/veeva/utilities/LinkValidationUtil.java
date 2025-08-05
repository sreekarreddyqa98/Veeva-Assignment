/**
 * LinkValidationUtil - URL Accessibility and Link Validation Utility
 * 
 * This utility class provides methods to validate URL accessibility and check
 * if web links are working properly. It uses RestAssured library to perform
 * HTTP requests and validate response status codes.
 * 
 * Key Features:
 * - URL accessibility validation using HTTP GET requests
 * - Support for both HTTP and HTTPS URLs with relaxed SSL validation
 * - Status code validation (200 OK, 403 Forbidden considered valid)
 * - Comprehensive error handling for network issues
 * - Static methods for easy integration in test scenarios
 * 
 * Common Use Cases:
 * - Validating footer links on web pages
 * - Checking external link accessibility
 * - Verifying API endpoint availability
 * - Link health checks in automated tests
 * - Broken link detection
 * 
 * Supported Status Codes:
 * - 200 (OK): Link is accessible and working
 * - 403 (Forbidden): Link exists but access is restricted (considered valid)
 * - Other codes: Considered as broken or inaccessible links
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LinkValidationUtil {
    
    // Static Response object to store the last HTTP response for debugging
    static Response response;
    
    /**
     * Validates if a URL is accessible and working
     * 
     * This method performs an HTTP GET request to the specified URL and checks
     * the response status code to determine if the link is working. It uses
     * relaxed HTTPS validation to handle SSL certificate issues.
     * 
     * Validation Logic:
     * - Status Code 200 (OK): Link is fully accessible
     * - Status Code 403 (Forbidden): Link exists but access restricted (valid)
     * - Other status codes: Link is considered broken or inaccessible
     * - Network exceptions: Link is considered broken
     * 
     * Features:
     * - Relaxed HTTPS validation to handle self-signed certificates
     * - Exception handling for network connectivity issues
     * - Support for both HTTP and HTTPS protocols
     * - Response caching for debugging purposes
     * 
     * @param url The URL to validate (must include protocol: http:// or https://)
     * @return true if URL is accessible (status 200 or 403), false otherwise
     * 
     * Usage Examples:
     * - boolean isWorking = LinkValidationUtil.isUrlWorking("https://example.com");
     * - boolean isValid = LinkValidationUtil.isUrlWorking("http://api.example.com/health");
     */
    public static boolean isUrlWorking(String url) {
        try {
            // Configure RestAssured to use relaxed HTTPS validation
            // This allows testing of URLs with self-signed or invalid SSL certificates
            RestAssured.useRelaxedHTTPSValidation();
            
            // Perform HTTP GET request to the specified URL
            response = RestAssured
                    .given()
                        // Enable relaxed HTTPS validation for this request
                        .relaxedHTTPSValidation()
                    .when()
                        // Send GET request to the URL
                        .get(url)
                    .then()
                        // Extract the response for status code checking
                        .extract()
                        .response();
            
            // Get the HTTP status code from the response
            int statusCode = response.getStatusCode();
            
            // Consider URL working if status is 200 (OK) or 403 (Forbidden)
            // 403 is considered valid because the URL exists but access is restricted
            return (statusCode == 200) || (statusCode == 403);
            
        } catch (Exception e) {
            // Handle any exceptions during HTTP request (network issues, timeouts, etc.)
            // Return false for any exception (URL is considered broken)
            return false;
        }
    }
}
