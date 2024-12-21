package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private WebDriver driver;

    @FindBy(xpath = "//input[@name='username']")
    private WebElement userNameTextBox;

    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement passwordTextBox;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[normalize-space()='Invalid credentials']")
    private WebElement invalidCredentialsText;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public void enterUSerName(String uName){
        userNameTextBox.sendKeys(uName);
    }
    public void enterPassword(String password){
        passwordTextBox.sendKeys(password);
    }
    public void clickLoginButton(){
        loginButton.click();
    }
    public String getInvalidCredentialsText(){
        return invalidCredentialsText.getText();
    }
}
