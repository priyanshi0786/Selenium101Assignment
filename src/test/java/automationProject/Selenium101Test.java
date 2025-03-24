package automationProject;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import org.testng.*;

public class Selenium101Test {

    public RemoteWebDriver driver;
    public WebDriverWait wait;

    String ltUsername = "saxenapriyanshi031";
    String ltAccessKey = "LT_QP0Tn7qN6InYxb0Is5mvBli89susBigInMHHc3gqy056WhE";

    @BeforeClass
    @Parameters({"browser", "version", "platform"})
    public void setup(String browser, String version, String platform) throws MalformedURLException {
    	DesiredCapabilities capabilities = new DesiredCapabilities();
    	capabilities.setCapability("browserName", browser);
    	capabilities.setCapability("browserVersion", version);
    	capabilities.setCapability("platformName", platform);

    	capabilities.setCapability("LT:Options", new HashMap<String, Object>() {{
    	    put("user", ltUsername);
    	    put("accessKey", ltAccessKey);
    	    put("build", "Selenium 101 Assignment");
    	    put("name", "Selenium 101 Tests");
    	    put("w3c", true);
    	    put("plugin", "java-testNG");
    	}});

        driver = new RemoteWebDriver(new URL("https://" + ltUsername + ":" + ltAccessKey + "@hub.lambdatest.com/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testScenario1() {
        driver.get("https://www.lambdatest.com/selenium-playground");
        driver.findElement(By.linkText("Simple Form Demo")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("simple-form-demo"), "URL Validation Failed");

        String message = "Welcome to LambdaTest";
        driver.findElement(By.id("user-message")).sendKeys(message);
        driver.findElement(By.id("showInput")).click();

        String displayedMessage = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(displayedMessage, message, "Message Validation Failed");
    }

    @Test
    public void testScenario2() throws InterruptedException {
        driver.get("https://www.lambdatest.com/selenium-playground");
//        Thread.sleep(3000);
        driver.findElement(By.linkText("Drag & Drop Sliders")).click();
//        Thread.sleep(3000);
        WebElement slider = driver.findElement(By.xpath("//input[@value='15']"));
        WebElement sliderValue = driver.findElement(By.id("rangeSuccess"));
        slider.click();

        for (int i = 0; i < 45; i++) {  
            slider.sendKeys(Keys.ARROW_RIGHT);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElement(sliderValue, "95"));

        String rangeValue = sliderValue.getText();
        Assert.assertEquals(rangeValue, "95", "Slider Value Validation Failed");
    }
    

    @Test
    public void testScenario3() throws InterruptedException {
        driver.get("https://www.lambdatest.com/selenium-playground");
        driver.findElement(By.linkText("Input Form Submit")).click();

        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        Thread.sleep(2000);
        List<WebElement> inputFields = driver.findElements(By.xpath("//input | //textarea | //select"));

     String validationMessage = null;
     for (WebElement field : inputFields) {
         boolean isValid = (Boolean) ((JavascriptExecutor) driver)
                 .executeScript("return arguments[0].checkValidity();", field);

         if (!isValid) {
             validationMessage = (String) ((JavascriptExecutor) driver)
                     .executeScript("return arguments[0].validationMessage;", field);
             break;
         }
     }

     // Assert if validation message is retrieved
     Assert.assertNotNull(validationMessage, "No validation message found!");
     Assert.assertEquals(validationMessage, "Please fill out this field.");
        WebElement name = driver.findElement(By.name("name"));
        name.sendKeys("Priyanshi");
        Thread.sleep(2000);
        name.sendKeys(Keys.TAB);
        Thread.sleep(1000);
        WebElement emailfield = driver.findElement(By.id("inputEmail4"));
//        emailfield.click();
        emailfield.sendKeys("saxenapriyanshi031@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Test@123");
        driver.findElement(By.name("company")).sendKeys("LambdaTest");
        driver.findElement(By.name("website")).sendKeys("https://www.lambdatest.com");

        Select countryDropdown = new Select(driver.findElement(By.name("country")));
        countryDropdown.selectByVisibleText("United States");

        driver.findElement(By.name("city")).sendKeys("New York");
        driver.findElement(By.name("address_line1")).sendKeys("123 Main St");
        driver.findElement(By.name("address_line2")).sendKeys("Suite 456");
        driver.findElement(By.id("inputState")).sendKeys("NY");
        driver.findElement(By.name("zip")).sendKeys("10001");

        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Thanks for contacting us, we will get back to you shortly.']")));
        Assert.assertTrue(successMsg.getText().contains("Thanks for contacting us"), "Form submission success message validation failed");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
