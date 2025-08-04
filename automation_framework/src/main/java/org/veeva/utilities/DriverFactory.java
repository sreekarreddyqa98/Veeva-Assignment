//package org.veeva.utilities;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//
//public class DriverFactory {
//    public static WebDriver driver;
//
//    public static WebDriver initDriver() {
//        try {
//            String browser = ConfigReader.getProperty("browser");
//
//            if (browser.equalsIgnoreCase("chrome")) {
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--disable-blink-features=AutomationControlled");
//                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
//                options.setExperimentalOption("useAutomationExtension", false);
//                options.addArguments("--start-maximized");
//                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                        + "(KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
//                options.addArguments("--window-size=3840,2160");
//                options.addArguments("--headless=new"); // or just "--headless" if your Chrome version doesn't support "--headless=new"
//                options.addArguments("--disable-gpu");  // recommended for headless mode
//
//                driver = new ChromeDriver(options);
//            }
//
//            if (browser.equalsIgnoreCase("edge")) {
//                driver = new EdgeDriver();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return driver;
//    }
//
//    public static WebDriver getDriver() {
//        return driver;
//    }
//}

package org.veeva.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        try {
            String browser = ConfigReader.getProperty("browser");

            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--start-maximized");
                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
                options.addArguments("--window-size=3840,2160");
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");

                driver.set(new ChromeDriver(options));
                System.setProperty("browser.name", browser);

            }

            if (browser.equalsIgnoreCase("firefox")) {
                System.setProperty("webdriver.firefox.driver", "path/to/geckodriver.exe"); // optional if Selenium Manager or WebDriverManager is used

                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-maximized");
                options.addArguments("--width=3840");
                options.addArguments("--height=2160");
//                options.addArguments("--headless"); // Firefox uses just "--headless"
//                options.addArguments("--disable-gpu"); // Not mandatory for Firefox but can be included
                options.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

                driver.set(new FirefoxDriver(options));
                System.setProperty("browser.name", browser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
