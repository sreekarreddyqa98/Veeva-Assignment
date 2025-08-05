/**
 * RetryHandler - Method Execution Retry Mechanism
 * 
 * This utility class provides a robust retry mechanism for method execution
 * using Java Reflection API and functional interfaces. It automatically retries 
 * failed method calls based on @RetryStep annotation configuration.
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import java.lang.reflect.Method;

public class RetryHandler {
    
    /**
     * Executes a Runnable with retry logic based on caller method's @RetryStep annotation
     */
    public static void executeWithRetry(Runnable action) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerMethodName = stackTrace[2].getMethodName();
        String callerClassName = stackTrace[2].getClassName();
        
        try {
            Class<?> callerClass = Class.forName(callerClassName);
            Method[] methods = callerClass.getDeclaredMethods();
            
            Method callerMethod = null;
            for (Method method : methods) {
                if (method.getName().equals(callerMethodName)) {
                    callerMethod = method;
                    break;
                }
            }
            
            if (callerMethod != null && callerMethod.isAnnotationPresent(RetryStep.class)) {
                RetryStep retryStep = callerMethod.getAnnotation(RetryStep.class);
                int attempts = retryStep.attempts();
                executeWithRetryAttempts(action, attempts, callerMethodName);
            } else {
                action.run();
            }
        } catch (Exception e) {
            action.run();
        }
    }
    
    /**
     * Core retry logic for Runnable actions
     */
    private static void executeWithRetryAttempts(Runnable action, int maxAttempts, String methodName) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < maxAttempts) {
            try {
                action.run();
                return;
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                if (attempts < maxAttempts) {
                    System.out.println("Retrying method: " + methodName + " (attempt " + (attempts + 1) + "/" + maxAttempts + ")");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Method " + methodName + " failed after " + maxAttempts + " attempts", lastException);
    }
    
    /**
     * Legacy method - kept for backward compatibility
     */
    public static void invokeWithRetry(Object obj, Method method, Object... args) throws Throwable {
        RetryStep retryStep = method.getAnnotation(RetryStep.class);
        int attempts = retryStep != null ? retryStep.attempts() : 1;
        int tries = 0;

        while (tries < attempts) {
            try {
                method.invoke(obj, args);
                return;
            } catch (Exception e) {
                tries++;
                if (tries >= attempts) {
                    throw e.getCause() != null ? e.getCause() : e;
                }
                Thread.sleep(2000);
            }
        }
    }
}