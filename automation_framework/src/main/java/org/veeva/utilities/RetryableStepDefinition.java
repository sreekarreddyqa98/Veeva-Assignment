/**
 * RetryableStepDefinition - Base class for Step Definitions with Retry Support
 * 
 * This abstract base class provides automatic retry functionality for step definitions.
 * Step definition classes should extend this class to get automatic retry behavior
 * for methods annotated with @RetryStep.
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.lang.reflect.Method;

public abstract class RetryableStepDefinition {
    
    /**
     * Executes a method with retry logic based on @RetryStep annotation
     * 
     * @param methodName Name of the method to execute
     * @param args Arguments to pass to the method
     * @throws Throwable if all retry attempts fail
     */
    protected void executeWithRetry(String methodName, Object... args) throws Throwable {
        Method method = this.getClass().getMethod(methodName, getParameterTypes(args));
        RetryHandler.invokeWithRetry(this, method, args);
    }
    
    /**
     * Helper method to get parameter types from arguments
     */
    private Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}