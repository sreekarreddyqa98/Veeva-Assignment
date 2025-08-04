package org.veeva.stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.veeva.pageObjects.Pages.HomePage;
import org.veeva.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;

import static org.veeva.DP1_Utils.FileUtils.readExpectedTitlesFromFile;
import static org.veeva.DP1_Utils.FileUtils.writeActualTitlesToFile;

public class DerivedProductSteps {

    public WebDriver driver;
    public WebDriverWait wait;
    public HomePage homePage;
    public Utilities utils;
    ElementFetcher elementFetcher;
    private Map<String, Object> pageObjectMap = new HashMap<>();
    private Object Page;
    private int elementCount;
    public DerivedProductSteps() {
        this.driver = Hooks.getDriver(); // from Hooks
        this.wait = Hooks.getWait();     // from Hooks
        this.homePage = new HomePage(driver);
        this.elementFetcher = new ElementFetcher(driver);
        this.utils = new Utilities(driver);

        pageObjectMap.put("HomePage", homePage);
    }

    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        driver.get(ConfigReader.getProperty(url));

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



    @Given("I count the instances of {string} from {string} page")
    public void i_count_the_instances_of(String elementName, String pageName) {
        Page = pageObjectMap.get(pageName);
        List<WebElement> elements = elementFetcher.getElementByFieldName(Page,elementName);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        elementCount = utils.getCountOfElements(elements);
    }

    @Then("I get the title of each slide and validate with expected data from file")
    public void validate_slide_titles_from_file() {
        String expectedFilePath = "src/test/resources/test_data/expected_titles.txt";
        String actualFilePath = "src/test/resources/test_data/actual_titles.txt";

        List<WebElement> slides = homePage.getSlideTitles();

        List<String> actualTitles = new ArrayList<>();
        AllureReportUtils.attachTextToAllure("Slide Titles found are as below","");
        for (WebElement slide : slides) {
            actualTitles.add(slide.getText().trim());
            AllureReportUtils.attachTextToAllure("Slide Title : ",slide.getText().trim());
        }

        writeActualTitlesToFile(actualTitles, actualFilePath);

        List<String> expectedTitles = readExpectedTitlesFromFile(expectedFilePath);

        Assert.assertEquals(expectedTitles, actualTitles);
    }


    @Then("I validate element count to be {string}")
    public void validate_element_count(String count){
        Assert.assertEquals(elementCount,Integer.parseInt(count),"Slide Count is Mismatch : Actual = "+String.valueOf(elementCount)+" Expected = "+Integer.parseInt(count));
    }

    @Then("I validate the slide durations should be equal to {string} seconds")
    public void validate_slide_duration(String seconds){
        Map<String, Long> slideDurations = homePage.trackSlideDurations();
        for (Map.Entry<String, Long> slide : slideDurations.entrySet()) {
            Assert.assertEquals(slide.getValue().intValue(), Integer.parseInt(seconds), "Duration mismatch for slide: " + slide.getKey());
            AllureReportUtils.attachTextToAllure("Slide Title",slide.getKey());
            AllureReportUtils.attachTextToAllure("Actual Slide Duration",String.valueOf(slide.getValue().intValue()));
        }
    }

}
