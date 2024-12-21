package Base;

import Drivers.DriverHolder;
import Drivers.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.javafaker.Faker;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public static ExtentReports extent;
    public static ExtentTest test;
    private Properties config;
    private Faker faker;
    private String validUserName;
    private String validPassword;
    private String baseUrl;

    @BeforeSuite(alwaysRun = true)
    public void setUpClass() throws IOException {
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/extentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        config = new Properties();
        FileInputStream configFile = new FileInputStream("config.properties");
        config.load(configFile);

        validUserName = config.getProperty("validUsername");
        validPassword = config.getProperty("validPassword");
        baseUrl = config.getProperty("baseURL");

        faker = new Faker();
    }

    public String getValidUserName() {
        return validUserName;
    }

    public String getValidPassword() {
        return validPassword;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
    public String getInvalidUserName() {
        return faker.name().username();
    }

    public String getInvalidPassword() {
        return faker.internet().password();
    }

    public String takeScreenshot(WebDriver driver, String filename) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        Date date = new Date();
        String timestamp = formatter.format(date);

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String screenshotDir = System.getProperty("user.dir") + "/screenshots";
        File dir = new File(screenshotDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String relativePath = "./screenshots/" + filename + "_" + timestamp + ".png";
        File destinationFile = new File(screenshotDir, filename + "_" + timestamp + ".png");
        FileHandler.copy(sourceFile, destinationFile);

        return relativePath;
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("default") String browser, Method method){
        driver = DriverManager.getNewInstance(browser);
        DriverHolder.setDriver(driver);

        DriverHolder.getDriver().manage().window().maximize();
        DriverHolder.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        DriverHolder.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        wait = new WebDriverWait(DriverHolder.getDriver(),Duration.ofSeconds(10));

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                String screenshotPath = takeScreenshot(driver, result.getName());

                String screenshotHtml = "<a href='" + screenshotPath + "' data-featherlight='image'>" +
                        "<img src='" + screenshotPath + "' alt='Test Screenshot' width='500' height='300'/>" +
                        "</a>";

                test.fail("Test failed, screenshot attached." + screenshotHtml,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

                Throwable failureReason = result.getThrowable();
                if (failureReason != null) {
                    test.fail("Failure reason: " + failureReason.getMessage());
                    test.fail(failureReason);
                }
            } catch (IOException e) {
                test.fail("Failed to capture screenshot on failure: " + e.getMessage());
            }
        }

        if (DriverHolder.getDriver() != null) {
            DriverHolder.getDriver().quit();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownClass(){
        if (extent != null) {
            extent.flush();
        }
    }
}
