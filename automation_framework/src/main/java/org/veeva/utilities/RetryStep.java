package org.veeva.utilities;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryStep {
    int attempts() default 2;
}
