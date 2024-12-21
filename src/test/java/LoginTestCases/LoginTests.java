package LoginTestCases;

import Base.BaseTest;
import Pages.LoginPage;
import Utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    private LoginPage loginPage;
    private WaitUtils waitUtils;


    @BeforeMethod
    public void openPage(){
        driver.get(getBaseUrl());
        loginPage = new LoginPage(driver);
        waitUtils = new WaitUtils(driver);
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData(){
        return new Object[][]{
                {getValidUserName(), getValidPassword(), "pass", "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index"},
                {getInvalidUserName(), getInvalidPassword(), "fail", "Invalid credentials"},
                {getInvalidUserName(), getValidPassword(), "fail", "Invalid credentials"},
                {getValidUserName(), getInvalidPassword(), "fail", "Invalid credentials"}
        };
    }

    @Test(dataProvider = "loginData")
    public void LoginTest(String userName, String password, String outcome, String expected){
        test = extent.createTest("Test Login with Username: " + userName + " and Outcome: " + outcome);

        try{
            loginPage.enterUSerName(userName);
            loginPage.enterPassword(password);
            loginPage.clickLoginButton();

            if (outcome.equals("pass")) {
                // Successful login scenario
                waitUtils.waitForUrlToBe(expected);
                String actualUrl = driver.getCurrentUrl();
                Assert.assertEquals(actualUrl, expected, "URL did not match the expected dashboard URL.");
                test.pass("Successfully logged in with valid credentials.");
            } else {
                // Error message validation for invalid credentials
                String actualErrorText = loginPage.getInvalidCredentialsText();
                String resultText = waitUtils.waitForText(actualErrorText, expected);
                Assert.assertEquals(resultText, expected, "Error message did not match for invalid credentials.");
                test.pass("Correct error message displayed for invalid credentials.");
            }
        }catch(Exception e){
            test.fail("Test failed due to an exception: " + e.getMessage());
            Assert.fail("Exception occurred: " + e.getMessage());
        }

    }
}
