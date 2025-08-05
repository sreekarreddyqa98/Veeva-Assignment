/**
 * RetryStep - Annotation for Method Retry Configuration
 * 
 * This custom annotation is used to mark methods that should be retried
 * automatically when they fail. It works in conjunction with RetryHandler
 * to provide configurable retry behavior for flaky operations.
 * 
 * Key Features:
 * - Runtime retention for reflection-based access
 * - Method-level targeting for precise control
 * - Configurable retry attempts with sensible defaults
 * - Simple annotation-driven configuration
 * 
 * Annotation Properties:
 * - attempts: Number of retry attempts (default: 2)
 * 
 * Common Use Cases:
 * - Marking flaky web element interactions for retry
 * - Configuring network-dependent operations
 * - Handling intermittent test failures
 * - Improving test stability without code changes
 * 
 * Usage Examples:
 * @RetryStep(attempts = 3)
 * public void clickSubmitButton() { ... }
 * 
 * @RetryStep  // Uses default 2 attempts
 * public void waitForElement() { ... }
 * 
 * Design Pattern: Annotation-Driven Configuration
 * Retention: RUNTIME (accessible via reflection)
 * Target: METHOD (can only be applied to methods)
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.lang.annotation.*;

// Retain annotation at runtime for reflection-based access
@Retention(RetentionPolicy.RUNTIME)
// Target methods only - this annotation can only be applied to methods
@Target(ElementType.METHOD)
public @interface RetryStep {
    
    /**
     * Number of retry attempts for the annotated method
     * 
     * Specifies how many times the method should be retried if it fails.
     * The total number of executions will be attempts + 1 (initial attempt).
     * 
     * Default value: 2 (method will be tried up to 3 times total)
     * Minimum value: 1 (method will be tried up to 2 times total)
     * 
     * @return Number of retry attempts
     */
    int attempts() default 2;
}
