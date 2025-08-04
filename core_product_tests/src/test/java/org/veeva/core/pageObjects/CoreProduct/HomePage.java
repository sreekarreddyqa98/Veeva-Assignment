package org.veeva.core.pageObjects.CoreProduct;

import org.openqa.selenium.*;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.veeva.utilities.AllureReportUtils;
import org.veeva.utilities.BaseClass;

public class HomePage extends BaseClass {


    @FindBy(id="onetrust-accept-btn-handler")
    @CacheLookup
    private WebElement cookieAcceptBtn;

    @FindBy(xpath="//div[text()='x']")
    @CacheLookup
    private WebElement signupCloseBtn;

    @FindBy(xpath="//div[@data-testid='display-ad']/parent::div")
    private WebElement homePageAdBanner;

    @FindBy(xpath="(//span[text()='Shop'])[1]")
    private WebElement shopMenuOption;


    @FindBy(xpath="(//a[contains(text(),'Men')])[1]")
    private WebElement mens_category;

    @FindBy(xpath="//li[@class='menu-item']//span[text()='...']")
    private WebElement moreOption;


    @FindBy(xpath="(//a[text()='News & Features'])[1]")
    private WebElement News_and_features;

    public HomePage(WebDriver driver) {
        super(driver);
    }


    public String returnPageTitle(){
        return driver.getTitle();
    }

    public void skipSIgnUp() {
        wait.until(ExpectedConditions.visibilityOf(signupCloseBtn));
        utils.clickIfVisible(signupCloseBtn);
    }

    public void acceptCookies(){
        try{
            wait.until(ExpectedConditions.visibilityOf(cookieAcceptBtn));
            utils.clickIfVisible(cookieAcceptBtn);
        }catch (Exception e){
            AllureReportUtils.attachTextToAllure("Info : ","Cookie Banner Not Found");
        }

    }

//
//    public void hoverToElemnt(WebElement element){
//        actions.moveToElement(element).perform();
//    }
//
//    public void clickElemnt(WebElement element){
//        wait.until(ExpectedConditions.visibilityOf(element));
//        actions.moveToElement(element).click().perform();
//    }



}
