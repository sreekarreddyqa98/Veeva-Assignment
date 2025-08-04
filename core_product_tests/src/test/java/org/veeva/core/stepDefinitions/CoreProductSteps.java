package org.veeva.core.stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.veeva.core.pageObjects.CoreProduct.HomePage;
import org.veeva.core.pageObjects.CoreProduct.MensShopPage;
import org.veeva.core.pageObjects.CoreProduct.NewsAndFeed;
import org.veeva.utilities.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreProductSteps {

    public WebDriver driver;
    public WebDriverWait wait;
    public HomePage homePage;
    public MensShopPage mensShopPage;
    public Utilities utils;
    ElementFetcher elementFetcher;
    private Map<String, Object> pageObjectMap = new HashMap<>();
    private Object Page;
    public NewsAndFeed newsAndFeedPage;
    private int elementCount;

    public CoreProductSteps() {
        this.driver = Hooks.getDriver(); // from Hooks
        this.wait = Hooks.getWait();     // from Hooks
        this.homePage = new HomePage(driver);
        this.mensShopPage = new MensShopPage(driver);
        this.newsAndFeedPage = new NewsAndFeed(driver);
        this.elementFetcher = new ElementFetcher(driver);
        this.utils = new Utilities(driver);

        pageObjectMap.put("HomePage", homePage);
        pageObjectMap.put("MensShopPage", mensShopPage);
        pageObjectMap.put("NewsAndFeedpage", newsAndFeedPage);
    }

    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        driver.get(ConfigReader.getProperty(url));

    }
    @Then("I skip signup")
    public void i_skip_signup() {
        homePage.skipSIgnUp();
    }
    @Then("I accept cookies")
    public void i_accept_cookies() {
        homePage.acceptCookies();
    }

    @Then("I hover to the {string} in {string} and click on {string}")
    @RetryStep(attempts = 3)
    public void i_hover_to_the_in(String elementName, String pageName, String clickElementName) throws InterruptedException {
        Page = pageObjectMap.get(pageName);
        WebElement element = elementFetcher.getElementByFieldName(Page,elementName);
        wait.until(ExpectedConditions.visibilityOf(element));
        utils.hoverToElement(element);
        WebElement clickElement = elementFetcher.getElementByFieldName(Page,clickElementName);
        wait.until(ExpectedConditions.elementToBeClickable(clickElement));
        utils.hoverToElement(element);
        utils.clickIfVisible(clickElement);
    }

    @Then("I click on {string} from page {string}")
    public void i_click_on(String elementName,  String pageName) {
        Page = pageObjectMap.get(pageName);
        WebElement element = elementFetcher.getElementByFieldName(Page,elementName);
        utils.clickIfVisible(element);
    }


    @Then("I switch to {string} page")
    public void i_switch_to_page(String pageTitle) {
        utils.switchToTabWithTitle(pageTitle);
    }


    @Then("I save data to text file as {string}")
    public void i_save_data_to_text_file_as(String filename) {
        mensShopPage.saveProductDetailsToTextFileFromCurrentPage(filename);
    }

    @Given("I count the instances of {string} from {string} page")
    public void i_count_the_instances_of(String elementName, String pageName) {
        Page = pageObjectMap.get(pageName);
        List<WebElement> elements = elementFetcher.getElementByFieldName(Page,elementName);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        elementCount = utils.getCountOfElements(elements);
    }

    @Then("I validate element count to be {string}")
    public void validate_element_count(String count){
        Assert.assertEquals(elementCount,Integer.parseInt(count),"Element Count is Mismatch : Actual = "+String.valueOf(elementCount)+" Expected = "+Integer.parseInt(count));
    }

    @Given("I count the videos with {string} older than {string} days")
    public void i_count_the_videos_with_older_than_days(String elementName, String days){
        List<WebElement> elements = newsAndFeedPage.getVideoUploadTImes();
        int count = utils.getCountOfElements(elements);

        List<Integer> dayCounts = elements.stream()
                .map(el -> el.getAttribute("aria-label"))
                .filter(label -> label != null && label.contains(" days"))
                .map(label -> label.substring(0, label.indexOf(" days")).trim())
                .map(Integer::parseInt)
                .filter(d -> d > Integer.parseInt(days))
                .toList();
        elementCount = dayCounts.size();
    }

    @Then("I extract all the product details")
    public void i_extract_all_the_product_details() {
        mensShopPage.getAllProductDetailsFromAllPages();
    }

    @And("I wait for {string} to be visible in {string} page")
    public void iWaitForToBeVisible(String elementName,String pageName) {
        Page = pageObjectMap.get(pageName);
        WebElement element = elementFetcher.getElementByFieldName(Page,elementName);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

}
