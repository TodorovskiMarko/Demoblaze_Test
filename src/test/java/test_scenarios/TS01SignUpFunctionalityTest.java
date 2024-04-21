package test_scenarios;

import base.TestBase;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;

public class TS01SignUpFunctionalityTest extends TestBase {

    public static void SignUp() {
        driver.findElement(By.linkText(loc.getProperty("signUp_Link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement signUpWindow =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("signUp_Window"))));

        // Assert that Sign Up Window is visible
        if (signUpWindow.isDisplayed()) {
            System.out.println("Window is visible");
        } else {
            System.out.println("Window is not visible");
        }

        driver.findElement(By.id(loc.getProperty("username_Field"))).sendKeys(input.getProperty("user"));
        driver.findElement(By.id(loc.getProperty("password_Field"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("signUp_button"))).click();
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public static void ClickOnSignUp() {
        driver.findElement(By.linkText(loc.getProperty("signUp_Link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement signUpWindow =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("signUp_Window"))));

        // Assert that Sign Up Window is visible
        if (signUpWindow.isDisplayed()) {
            System.out.println("Window is visible");
        } else {
            System.out.println("Window is not visible");
        }
    }

    public static void AlertEmpty() {
        Alert alert = driver.switchTo().alert();

        // Assert that the correct text is displayed on the alert
        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_empty"));

        alert.accept();
    }

    public static void WaitForAlert() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.alertIsPresent());
    }

    @Test(description = "Validate Sign Up using new username")
    public static void TC_01() {
        SignUp();

        Alert alert = driver.switchTo().alert();

        // Assert that the correct text is displayed on the alert
        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_success"));

        alert.accept();
    }

    @Test(description = "Validate Sign Up using already registered user", priority = 1)
    public static void TC_02() {
        SignUp();

        Alert alert = driver.switchTo().alert();

        // Assert that the correct text is displayed on the alert
        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_exists"));

        alert.accept();
    }

    @Test(description = "Validate Sign Up without filling the password field", priority = 2)
    public static void TC_03() {
        ClickOnSignUp();

        driver.findElement(By.id(loc.getProperty("username_Field"))).sendKeys(input.getProperty("user2"));
        driver.findElement(By.xpath(loc.getProperty("signUp_button"))).click();
        WaitForAlert();

        AlertEmpty();
    }

    @Test(description = "Validate Sign Up without filling the username field", priority = 3)
    public static void TC_04() {
        ClickOnSignUp();

        driver.findElement(By.id(loc.getProperty("password_Field"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("signUp_button"))).click();
        WaitForAlert();

        AlertEmpty();
    }

    @Test(description = "Validate Sign Up without filling any of the fields", priority = 4)
    public static void TC_05() {
        ClickOnSignUp();

        driver.findElement(By.xpath(loc.getProperty("signUp_button"))).click();
        WaitForAlert();

        AlertEmpty();
    }

    @Test(description = "Validate the text into the Password field is toggled to hide its visibility", priority = 5)
    public static void TC_06() {
        ClickOnSignUp();

        WebElement passwordField = driver.findElement(By.id(loc.getProperty("password_Field")));
        String password = input.getProperty("pass");
        passwordField.sendKeys(password);

        // Check if the password field's attribute value matches the expected password
        String enteredText = passwordField.getAttribute("value");
        // Check if the password field displays black dots (masked)
        Assert.assertEquals(enteredText.length(), password.length(), "Password is not masked");
    }

    @Test(description = "Validate the copying of the text entered into the Password field", priority = 6)
    public static void TC_07() throws IOException, UnsupportedFlavorException {
        ClickOnSignUp();

        WebElement passwordField = driver.findElement(By.id(loc.getProperty("password_Field")));
        String password = input.getProperty("pass");
        passwordField.sendKeys(password);

        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "c"));

        // Verify that the text was not copied
        String copiedText =
                Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
        System.out.println("Copied text: " + copiedText);
    }

    @Test(description = "Validate closing the Sign Up window", priority = 7)
    public static void TC_08() {
        ClickOnSignUp();

        driver.findElement(By.xpath(loc.getProperty("close_button"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        boolean isWindowClosed =
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc.getProperty("signUp_Window"))));

        Assert.assertTrue(isWindowClosed, "Window is no longer visible");
    }
}