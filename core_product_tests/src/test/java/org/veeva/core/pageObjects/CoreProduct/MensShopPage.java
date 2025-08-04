package org.veeva.core.pageObjects.CoreProduct;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.veeva.utilities.AllureReportUtils;
import org.veeva.utilities.BaseClass;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.veeva.utilities.Utilities.getElementXpathValue;


public class MensShopPage  extends BaseClass {

    private List<List<String>> collectedProductDeatils;

    @FindBy(xpath = "//div[@class='product-card-title']/a")
    private List<WebElement> productNames;

    @FindBy(xpath="/parent::div/preceding-sibling::div//span[@class='price']/span[@class='money-value']")
    private List<WebElement> productMRPPrices;

    @FindBy(xpath="/parent::div/preceding-sibling::div//span[@class='price primary']/span[@class='money-value']")
    private List<WebElement> productOfferPrices;

    @FindBy(xpath="/parent::div/following-sibling::div//span")
    private List<WebElement> productSellerTags;

    @FindBy(xpath="(//i[@class='icon icon-right-arrow'])[1]/parent::a")
    private WebElement nextPageButton;

    @FindBy(xpath="(//div[@class='pills-row']/a[text()='Jerseys'])[2]")
    private WebElement Jerseys;

    @FindBy(xpath="(//div[@class='pills-row']/a[text()='Hoodies & Sweatshirts'])[2]")
    private WebElement Hoodies_Sweatshirts;

    @FindBy(xpath="(//div[@class='pills-row']/a[text()='T-Shirts'])[2]")
    private WebElement t_shirts;

    @FindBy(xpath = "//span[text()='Jackets']")
    private WebElement Jackets_Filter;

    @FindBy(xpath = "//i[@aria-label='Close Pop-Up']")
    private WebElement offerBannerCloseBtn;

    public MensShopPage(WebDriver driver) {
        super(driver);
    }

    public void naviagteToMenShopPage(){
        utils.switchToTabWithTitle("Men's Golden State Warriors Gear, Mens Warriors Apparel, Guys Clothing | shop.warriors.com");
    }

    public String returnPageTitle(){
        return driver.getTitle();
    }


    public void saveProductDetailsToFile(List<List<String>> products, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (List<String> product : products) {
                writer.println(String.join(",", product));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to file: " + e.getMessage());
        }
    }


    public void saveProductDetailsToTextFileFromCurrentPage(String filename){
        List<List<String>> productData = collectedProductDeatils;
        filename = "src/test/resources/test_data/"+filename;
        saveProductDetailsToFile(productData, filename);
        AllureReportUtils.attachFileToAllure("Data File", filename, "text/plain");
    }

    public void getAllProductDetailsFromAllPages(){
        List<List<String>> allProducts = new ArrayList<>();

        while (true) {
            wait.until(ExpectedConditions.visibilityOfAllElements(productNames));

            int count = productNames.size();
            for (int i = 0; i < count; i++) {
                String name = productNames.get(i).getText().trim();
                int productIndex = i+1;
                String ProductPriceMRPXPath = "("+getElementXpathValue(MensShopPage.class, "productNames")+")[" + productIndex + "]"+getElementXpathValue(MensShopPage.class, "productMRPPrices");
                String ProductOfferPriceXPath = "("+getElementXpathValue(MensShopPage.class, "productNames")+")[" + productIndex + "]"+getElementXpathValue(MensShopPage.class, "productOfferPrices");
                String productNamesXpath  = "(("+getElementXpathValue(MensShopPage.class, "productNames")+")[" + productIndex + "]"+getElementXpathValue(MensShopPage.class, "productSellerTags")+")[1]";
                String sellerMessage = driver.findElements(By.xpath(productNamesXpath))
                        .stream()
                        .map(WebElement::getText)
                        .filter(text -> !text.isEmpty())
                        .reduce((a, b) -> a + "" + b)
                        .orElse("");
                String offerPrice = "";
                try{
                    offerPrice = driver.findElement(By.xpath(ProductOfferPriceXPath)).getText().trim();
                }
                catch (Exception e){
                    //Offer Price Not available
                }
                String mrpPrice = driver.findElement(By.xpath(ProductPriceMRPXPath)).getText().trim();
                String price = !offerPrice.isEmpty() ? offerPrice : mrpPrice;
                List<String> product = Arrays.asList(name, price, sellerMessage);
                allProducts.add(product);
            }

            // Check if the next page button is present and enabled
            try {
                // Check if nextButton is visible and enabled
                if (nextPageButton.getAttribute("aria-disabled").contains("true")) {
                    break;
                }

                nextPageButton.click();

            } catch (Exception e) {
                break; // Button not present or end of pagination
            }
        }
        collectedProductDeatils =  allProducts;
    }

    public void selectProductFilter(WebElement element){
        element.click();
    }

//    public String getElementXpathValue(String elementName) {
//        try {
//            Field field = this.getClass().getDeclaredField(elementName);
//            return field.getAnnotation(FindBy.class).xpath();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to get XPath", e);
//        }
//    }

}
