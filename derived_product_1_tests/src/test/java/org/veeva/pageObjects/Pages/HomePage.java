package org.veeva.pageObjects.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.veeva.utilities.AllureReportUtils;
import org.veeva.utilities.BaseClass;

import java.time.Instant;
import java.util.*;

public class HomePage extends BaseClass {

    @FindBy(xpath = "//button[text()='I Accept']")
    private WebElement cookieAcceptBtn;

    @FindBy(xpath = "//div[@role='tablist']/button")
    private List<WebElement> slides;

    @FindBy(xpath = "//div[@role='tablist']/button/div[1]")
    private List<WebElement> slidesTitles;

    @FindBy(xpath = "//div[contains(@aria-label,'Sign Up')]//button[@aria-label='Dismiss this popup']")
    private WebElement closeSignUp;

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

}
