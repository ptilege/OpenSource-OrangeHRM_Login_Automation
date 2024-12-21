package Drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {
    public static WebDriver getNewInstance(String browserName){
        switch(browserName.toLowerCase()){
            case "chrome-headless":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless--");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--disable-gpu--");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                FirefoxOptions options = new FirefoxOptions();
                options.addPreference("browser.startup.homepage", "about:blank");
                options.addPreference("browser.startup.page", 0);
                options.addPreference("permissions.default.image", 2);
                options.addPreference("dom.webnotifications.enabled", false);
                options.setLogLevel(FirefoxDriverLogLevel.TRACE);
                return new FirefoxDriver(options);
            case "firefox-headless":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--window-size=1920,1080");
                firefoxOptions.addArguments("--disable-gpu");
                firefoxOptions.addArguments("--disable-dev-shm-usage");
                return new FirefoxDriver(firefoxOptions);
            case "edge":
                return new EdgeDriver();
            default:
                chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service",false);
                prefs.put("profile_password_manager_enabled",false);
                prefs.put("profile.default_content_setting_values.notifications",2);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.directory_upgrade", true);
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--enable-incognito");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--no-proxy-server");
                chromeOptions.addArguments("remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-background-networking");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                chromeOptions.setExperimentalOption("prefs",prefs);
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-"});

                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                chromeOptions.merge(capabilities);

                return new ChromeDriver(chromeOptions);
        }
    }
}
