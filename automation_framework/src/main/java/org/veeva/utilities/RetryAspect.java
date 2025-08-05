/**
 * RetryAspect - AOP-based Method Retry Interceptor
 * 
 * This aspect intercepts methods annotated with @RetryStep and automatically
 * applies retry logic when they fail. It uses AspectJ to weave retry behavior
 * into annotated methods without requiring manual RetryHandler calls.
 * 
 * @author Sreekar Reddy
 * @version 1.0
 * @since 2024
 */
package org.veeva.utilities;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class RetryAspect {

    @Around("@annotation(org.veeva.utilities.RetryStep)")
    public Object retryOnFailure(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RetryStep retryStep = method.getAnnotation(RetryStep.class);
        
        int attempts = retryStep.attempts();
        int tries = 0;
        
        while (tries < attempts) {
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                tries++;
                if (tries >= attempts) {
                    throw e;
                }
                Thread.sleep(2000); // 2 second delay between retries
                System.out.println("Retrying method: " + method.getName() + " (attempt " + (tries + 1) + "/" + attempts + ")");
            }
        }
        return null;
    }
}