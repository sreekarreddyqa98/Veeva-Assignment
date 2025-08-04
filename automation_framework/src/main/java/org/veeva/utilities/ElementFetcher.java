package org.veeva.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ElementFetcher extends BaseClass{

    private  WebDriver driver;

    public ElementFetcher(WebDriver driver) {
        super(driver);
    }


    @SuppressWarnings("unchecked")
    public <T> T getElementByFieldName(Object pageObject, String fieldName) {
        try {
            Field field = pageObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(pageObject);

            if (value instanceof WebElement) {
                wait.until(ExpectedConditions.visibilityOf((WebElement) value));
            } else if (value instanceof List && !((List<?>) value).isEmpty()) {
                Object first = ((List<?>) value).getFirst();
                if (first instanceof WebElement) {
                    wait.until(ExpectedConditions.visibilityOf((WebElement) first));
                }
            }

            return (T) value;
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch field '" + fieldName + "' from " + pageObject.getClass().getSimpleName(), e);
        }
    }

}
