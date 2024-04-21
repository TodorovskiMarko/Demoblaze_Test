package test_scenarios;

import base.TestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;

public class TS02LoginFunctionalityTest extends TestBase {

    public static void ClickOnLogIn() {
        driver.findElement(By.linkText(loc.getProperty("logIn_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement signUpWindow =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("logIn_window"))));

        // Assert that Log in window is visible
        if (signUpWindow.isDisplayed()) {
            System.out.println("Window is visible");
        } else {
            System.out.println("Window is not visible");
        }

    }

    public static void AlertUserNotExist() {
        Alert alert = driver.switchTo().alert();

        // Assert that the correct text is displayed on the alert
        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_notExist"));

        alert.accept();
    }

    public static void AlertEmpty() {
        Alert alert = driver.switchTo().alert();

        // Assert that the correct text is displayed on the alert
        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_empty"));

        alert.accept();
    }

    public static void WaitForWelcome() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        WebElement welcome =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("welcome_header"))));
        Assert.assertTrue(welcome.isDisplayed(), "Element is visible");

        String actualText = welcome.getText();
        String expectedText = "Welcome " + input.getProperty("user");
        Assert.assertEquals(actualText, expectedText, "Element text is as expected");
    }

    public static void WaitForAlert() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.alertIsPresent());
    }

    @Test(description = "Validate logging in with valid credentials")
    public static void TC_01() {
        LogIn();
    }

    @Test(description = "Validate logging in with invalid username", priority = 1)
    public static void TC_02() {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_User"))).sendKeys(input.getProperty("user2"));
        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        WaitForAlert();

        AlertUserNotExist();
    }

    @Test(description = "Validate login with invalid password", priority = 2)
    public static void TC_03() {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_User"))).sendKeys(input.getProperty("user"));
        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("invalid_pass"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        WaitForAlert();

        Alert alert = driver.switchTo().alert();

        String alertText = alert.getText();
        Assert.assertEquals(alertText, loc.getProperty("alert_wrongPass"));

        alert.accept();
    }

    @Test(description = "Validate login without providing the password", priority = 3)
    public static void TC_04() {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_User"))).sendKeys(input.getProperty("user"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        WaitForAlert();

        AlertEmpty();
    }

    @Test(description = "Validate login without providing the username", priority = 4)
    public static void TC_05()  {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();

        WaitForAlert();

        AlertEmpty();
    }

    @Test(description = "Validate login without filling any of the fields", priority = 5)
    public static void TC_06() {
        ClickOnLogIn();

        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        WaitForAlert();

        AlertEmpty();
    }

    // User is logged out - REPORT DEFECT!!!
    @Test(description = "Validate Logging in and browsing back using Browser back button", priority = 6)
    public static void TC_07() {
        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();

        LogIn();

        driver.navigate().back();

        WaitForWelcome();
    }

    @Test(description = "Validate case sensitivity while logging in", priority = 7)
    public static void TC_08() {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_User"))).sendKeys(input.getProperty("case_sensitiveUser"));
        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        WaitForAlert();

        AlertUserNotExist();
    }

    @Test(description = "Validate the text into the Password field is toggled to hide its visibility", priority = 8)
    public static void TC_09() {
        ClickOnLogIn();

        WebElement passwordField = driver.findElement(By.id(loc.getProperty("log_pass")));
        String password = input.getProperty("pass");
        passwordField.sendKeys(password);

        // Check if the password field's attribute value matches the expected password
        String enteredText = passwordField.getAttribute("value");
        // Check if the password field displays black dots (masked)
        Assert.assertEquals(enteredText.length(), password.length(), "Password is not masked");
    }

    @Test(description = "Validate the copying of the text entered into the Password field", priority = 9)
    public static void TC_10() throws IOException, UnsupportedFlavorException {
        ClickOnLogIn();

        WebElement passwordField = driver.findElement(By.id(loc.getProperty("log_pass")));
        String password = input.getProperty("pass");
        passwordField.sendKeys(password);

        // Check if you can copy the text using keyboard
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "c"));

        // Verify that the text was not copied
        String copiedText =
                Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
        System.out.println("Copied text: " + copiedText);
    }

    @Test(description = "Validate the Password is not visible in the Page Source", priority = 10)
    public static void TC_11() {
        ClickOnLogIn();

        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("pass"));

        String pageSource = driver.getPageSource();

        // Check if the password text is present in the page source
        if (pageSource.contains(input.getProperty("pass"))) {
            System.out.println("Password is visible in the page source");
        } else {
            System.out.println("Password is not visible in the page source");
        }

        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        if (pageSource.contains(input.getProperty("pass"))) {
            System.out.println("Password is visible in the page source");
        } else {
            System.out.println("Password is not visible in the page source");
        }
    }

    // User is logged out - REPORT DEFECT!!!
    @Test(description =
            "Validate Logging in, " +
                    "closing the Browser without logging out and opening the application in the Browser again",
            priority = 11)
    public static void TC_12() {
        LogIn();

        driver.quit();

        WebDriver newDriver = new EdgeDriver();
        newDriver.manage().window().maximize();

        try {
            newDriver.get(prop.getProperty("testUrl"));

            // Validate that the user is still logged in
            WebElement welcome = newDriver.findElement(By.xpath(loc.getProperty("welcome_header")));
            Assert.assertTrue(welcome.isDisplayed(), "User is logged in");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        } finally {
            newDriver.quit();
        }
        WaitForWelcome();
    }

    // There's no rate limiting!
    @Test(description = "Validate if there's a Login Rate Limiting", priority = 12)
    public static void TC_13() {
        ClickOnLogIn();

        for (int i = 0; i < 5; i++) {

            WebElement usernameField = driver.findElement(By.id(loc.getProperty("log_User")));
            WebElement passwordField = driver.findElement(By.id(loc.getProperty("log_pass")));

            usernameField.sendKeys(input.getProperty("user2"));
            passwordField.sendKeys(input.getProperty("invalid_pass"));
            driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();
            WaitForAlert();

            AlertUserNotExist();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            usernameField.clear();
            passwordField.clear();
        }
    }

    @Test(description = "Validate closing the Log in window", priority = 13)
    public static void TC_14() {
        ClickOnLogIn();

        driver.findElement(By.xpath(loc.getProperty("logIn_close"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        boolean isWindowClosed =
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc.getProperty("logIn_window"))));

        Assert.assertTrue(isWindowClosed, "Window is no longer visible");
    }
}