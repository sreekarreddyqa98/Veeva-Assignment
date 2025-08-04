package org.veeva.core.pageObjects.CoreProduct;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.veeva.utilities.BaseClass;

import java.util.List;

public class NewsAndFeed extends BaseClass {


    @FindBy(xpath="//a[contains(@href, '/videos/') and @data-testid]")
    private List<WebElement> allVideoFeeds;

    @FindBy(xpath="//a[contains(@href, '/videos/') and @data-testid]/following::time[1]")
    private List<WebElement> uploadTimeOfVideos;

    public NewsAndFeed(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> getVideoUploadTImes(){
        return uploadTimeOfVideos;
    }

}
