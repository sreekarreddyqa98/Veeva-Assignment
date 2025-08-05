/**
 * RetryHandler - Method Execution Retry Mechanism
 * 
 * This utility class provides a robust retry mechanism for method execution
 * using Java Reflection API. It automatically retries failed method calls
 * based on @RetryStep annotation configuration, helping to handle flaky
 * test scenarios and intermittent failures.
 * 
 * Key Features:
 * - Automatic method retry based on @RetryStep annotation
 * - Configurable retry attempts per method
 * - Built-in delay between retry attempts
 * - Comprehensive exception handling and propagation
 * - Support for methods with variable arguments
 * 
 * Common Use Cases:
 * - Handling flaky web element interactions
 * - Retrying network-dependent operations
 * - Managing intermittent test failures
 * - Improving test stability and reliability
 * 
 * Usage Pattern:
 * 1. Annotate methods with @RetryStep(attempts = n)
 * 2. Use RetryHandler.invokeWithRetry() to execute methods
 * 3. Handler automatically retries on failure up to specified attempts
 * 
 * Design Pattern: Retry Pattern + Annotation-Driven Configuration
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.lang.reflect.Method;

public class RetryHandler {
    
    /**
     * Invokes a method with automatic retry mechanism
     * 
     * This method executes the specified method with retry logic based on
     * the @RetryStep annotation. If the method fails, it will be retried
     * up to the specified number of attempts with a delay between retries.
     * 
     * Retry Logic:
     * 1. Check if method has @RetryStep annotation
     * 2. Extract retry attempts from annotation (default: 1)
     * 3. Execute method and catch exceptions
     * 4. If method fails and retries remain, wait and retry
     * 5. If all retries exhausted, throw the original exception
     * 
     * Features:
     * - Configurable retry attempts via annotation
     * - 500ms delay between retry attempts
     * - Preserves original exception for debugging
     * - Supports methods with variable arguments
     * 
     * @param obj The object instance on which to invoke the method
     * @param method The Method object to invoke (obtained via reflection)
     * @param args Variable arguments to pass to the method
     * 
     * @throws Throwable The original exception if all retry attempts fail
     * 
     * Usage Example:
     * Method clickMethod = pageObject.getClass().getMethod("clickButton");
     * RetryHandler.invokeWithRetry(pageObject, clickMethod);
     */
    public static void invokeWithRetry(Object obj, Method method, Object... args) throws Throwable {
        // Check if method has @RetryStep annotation
        RetryStep retryStep = method.getAnnotation(RetryStep.class);
        
        // Get retry attempts from annotation, default to 1 if no annotation
        int attempts = retryStep != null ? retryStep.attempts() : 1;
        
        // Initialize retry counter
        int tries = 0;

        // Retry loop - continue until success or max attempts reached
        while (tries < attempts) {
            try {
                // Attempt to invoke the method with provided arguments
                method.invoke(obj, args);
                
                // If method executes successfully, return immediately
                return;
                
            } catch (Exception e) {
                // Increment try counter
                tries++;
                
                // If this was the last attempt, throw the exception
                if (tries >= attempts) {
                    // Throw the root cause exception for better debugging
                    throw e.getCause() != null ? e.getCause() : e;
                }
                
                // Wait 500ms before next retry attempt
                // This delay helps with timing-related issues
                Thread.sleep(500);
            }
        }
    }
}
