package org.veeva.utilities;

import java.lang.reflect.Method;

public class RetryHandler {
    public static void invokeWithRetry(Object obj, Method method, Object... args) throws Throwable {
        RetryStep retryStep = method.getAnnotation(RetryStep.class);
        int attempts = retryStep != null ? retryStep.attempts() : 1;
        int tries = 0;

        while (tries < attempts) {
            try {
                method.invoke(obj, args);
                return; // success
            } catch (Exception e) {
                tries++;
                if (tries >= attempts) {
                    throw e.getCause(); // rethrow root cause
                }
                Thread.sleep(500); // optional delay
            }
        }
    }
}
