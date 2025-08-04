package org.veeva.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    protected Object currentPage;
    protected Map<String, Object> pageObjectMap = new HashMap<>();
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public Utilities(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void hoverToElement(WebElement element){
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            actions.moveToElement(element).perform();
            Thread.sleep(Duration.ofMillis(1000));

        } catch (StaleElementReferenceException e) {
            // Retry once
            hoverToElement(element);
        } catch (TimeoutException e) {
            throw new RuntimeException("Timed out waiting for hover elements", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during hover to element", e);
        }
    }

    public void clickIfVisible(WebElement element) {
        try {

            wait.until(ExpectedConditions.visibilityOf(element));
            element.click();
        } catch (Exception catched_exception) {
            AllureReportUtils.attachTextToAllure("Exception : ",catched_exception.getMessage());
        }
    }

    public void scrollToPageEnd(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public int getCountOfElements(List<WebElement> elementList){
        return elementList.size();
    }



    public void switchToTabWithTitle(String expectedTitle) {
        String originalHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            driver.navigate().refresh();
            try {
                if (wait.until(ExpectedConditions.titleContains(expectedTitle))) {
                    return;
                }
            } catch (TimeoutException ignored) {
            }
        }
        // Fallback if not found, return to original tab
        driver.switchTo().window(originalHandle);
        throw new RuntimeException("No tab found with title containing: " + expectedTitle);
    }

    private void updateCurrentPageByTitle(String title) {
        for (Map.Entry<String, Object> entry : pageObjectMap.entrySet()) {
            Object pageObj = entry.getValue();
            String pageTitle = getPageTitleFromPageObject(pageObj);
            if (pageTitle != null && title.contains(pageTitle)) {
                currentPage = pageObj;
                return;
            }
        }
    }

    private String getPageTitleFromPageObject(Object pageObj) {
        try {
            Method method = pageObj.getClass().getMethod("getPageTitle");
            return (String) method.invoke(pageObj);
        } catch (Exception e) {
            return null;
        }
    }
    public static String getElementXpathValue(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            FindBy findBy = field.getAnnotation(FindBy.class);
            if (findBy != null && !findBy.xpath().isEmpty()) {
                return findBy.xpath();
            }
            throw new RuntimeException("Field is not annotated with @FindBy(xpath)");
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
    }




}
