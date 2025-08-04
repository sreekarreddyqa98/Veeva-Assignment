package org.veeva.dp2.pageObjects.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.veeva.utilities.AllureReportUtils;
import org.veeva.utilities.BaseClass;
import org.veeva.utilities.LinkValidationUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.veeva.utilities.Utilities.getElementXpathValue;

public class HomePage extends BaseClass {

    @FindBy(xpath = "//button[text()='I Accept']")
    private WebElement cookieAcceptBtn;

    @FindBy(xpath = "//div[@role='tablist']/button")
    private List<WebElement> slides;

    @FindBy(xpath = "//div[@role='tablist']/button/div[1]")
    private List<WebElement> slidesTitles;

    @FindBy(xpath = "//footer[@data-testid='footer']/div[2]//nav//h2")
    private List<WebElement> footerLinkCategories;

    @FindBy(xpath = "/following-sibling::ul//a")
    private List<WebElement> footerLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String returnPageTitle() {
        return driver.getTitle();
    }

    public void acceptCookies() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cookieAcceptBtn));
            utils.clickIfVisible(cookieAcceptBtn);
        } catch (Exception e) {
            AllureReportUtils.attachTextToAllure("Info : ","Cookie Banner Not Found");
        }
    }


    public List<WebElement>  getSlideTitles(){
        return slidesTitles;
    }

    public Map<String, Long> trackSlideDurations() {
        Map<String, Long> slideDurations = new LinkedHashMap<>();

        for (int run = 0; run < 2; run++) {
            int currentIndex = -1;
            Instant startTime = null;
            long[] durations = new long[slides.size()];

            while (true) {
                boolean activeFound = false;
                for (int i = 0; i < slides.size(); i++) {
                    if ("true".equals(slides.get(i).getAttribute("aria-selected"))) {
                        activeFound = true;
                        if (currentIndex != i) {
                            if (currentIndex != -1 && startTime != null && run == 1) {
                                long seconds = (Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000;
                                durations[currentIndex] += seconds;
                            }
                            currentIndex = i;
                            startTime = Instant.now();
                        }
                        break;
                    }
                }

                if (!activeFound) break;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                // Exit if all durations are non-zero in second run
                if (run == 1 && Arrays.stream(durations).noneMatch(d -> d == 0)) break;
            }

            // Capture last slide timing
            if (run == 1 && currentIndex != -1 && startTime != null) {
                long seconds = (Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000;
                durations[currentIndex] += seconds;
            }

            if (run == 1) {
                for (int i = 0; i < slides.size(); i++) {
                    String title;
                    try {
                        title = slidesTitles.get(i).getText().trim();
                        if (title.isEmpty()) title = "Slide " + (i + 1);
                    } catch (Exception e) {
                        title = "Slide " + (i + 1);
                    }
                    slideDurations.put(title, durations[i]);
                }
            }
        }

        return slideDurations;
    }

    public void getFooterCategoryLinks(String filename) throws IOException {
        filename = "src/test/resources/test_data/"+filename;
        FileWriter csvWriter = new FileWriter(filename);
        csvWriter.append("Category,Link Text,Href\n");
        wait.until(ExpectedConditions.visibilityOfAllElements(footerLinkCategories));
        int Categorycount = footerLinkCategories.size();
        for (int i = 0; i < Categorycount; i++) {
            String categoryName = footerLinkCategories.get(i).getText().trim();
            int linkIndex = i+1;
            String linkXpath = "("+getElementXpathValue(HomePage.class, "footerLinkCategories")+")[" + linkIndex + "]"+getElementXpathValue(HomePage.class, "footerLinks");
            List<WebElement> links = driver.findElements(By.xpath(linkXpath));
            for (WebElement link:links){
                String linktext = link.getText().trim();
                String href = link.getAttribute("href");
                csvWriter.append(categoryName).append(",").append(linktext).append(",").append(href).append("\n");
            }
        }
        csvWriter.flush(); // ✅ Ensure everything is written
        csvWriter.close(); // ✅ Close writer to release resources
        AllureReportUtils.attachFileToAllure("Data File", filename, "text/csv");
    }

    public void validateLinksFromCSV(String filename) throws IOException {
        filename = "src/test/resources/test_data/"+filename;
        String line;
        String csvSplitBy = ",";
        int brokenLinks = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy, -1); // Include empty values

                if (values.length < 3) continue;

                String category = values[0].trim();
                String linkText = values[1].trim();
                String linkUri = values[2].trim();

                if (!linkUri.isEmpty()) {
                    boolean isWorking = LinkValidationUtil.isUrlWorking(linkUri);

                    if (!isWorking) {
                        brokenLinks += 1;
                        // Capture broken link data
                        AllureReportUtils.attachTextToAllure("Broken Link Found:","");
                        AllureReportUtils.attachTextToAllure("Category",category);
                        AllureReportUtils.attachTextToAllure("LinkText",linkText);
                        AllureReportUtils.attachTextToAllure("LinkURI",linkUri);
                        AllureReportUtils.attachTextToAllure("--------------------------------","---------------------------------");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(brokenLinks,0,"Broken Link Found");
    }

    public void duplicateLinkChecker(String filename){
        filename = "src/test/resources/test_data/"+filename;
        String line;
        String csvSplitBy = ",";
        int duplicateLinksCount = 0;

        // Map to track LinkURI and corresponding rows
        Map<String, List<String[]>> linkMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            // Read header
            String header = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy, -1);
                if (values.length < 3) continue;

                String linkURI = values[2].trim();

                // Group rows by LinkURI
                linkMap.computeIfAbsent(linkURI, k -> new ArrayList<>()).add(values);
            }

            boolean foundDuplicate = false;
            for (Map.Entry<String, List<String[]>> entry : linkMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    duplicateLinksCount +=1;
                    foundDuplicate = true;
                    AllureReportUtils.attachTextToAllure("Duplicate LinkURI found: ",entry.getKey());
                    for (String[] row : entry.getValue()) {
                        AllureReportUtils.attachTextToAllure("Category: " + row[0] + ", LinkText: " + row[1],", LinkURI: " + row[2]);
                    }
                    AllureReportUtils.attachTextToAllure("--------------------------------","---------------------------------");
                }
            }

            if (!foundDuplicate) {
                AllureReportUtils.attachTextToAllure("Duplicate Links : ","Not Found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(duplicateLinksCount,0,"Duplicate Links Found");
    }

}
