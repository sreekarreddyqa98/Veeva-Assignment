package org.veeva.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseClass {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected Utilities utils;
    protected AllureReportUtils allureUtil;

    public BaseClass(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.utils = new Utilities(driver);
        this.allureUtil = new AllureReportUtils();

    }
}

