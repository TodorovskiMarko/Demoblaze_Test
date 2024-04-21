package test_scenarios;

import base.TestBase;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TS08ContactTest extends TestBase {
    private static void sendMessage(String email, String name, String message, String expectedAlertMessage) {
        driver.findElement(By.linkText(loc.getProperty("contact_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(loc.getProperty("contactEmail_field"))));

        driver.findElement(By.id(loc.getProperty("contactEmail_field"))).sendKeys(input.getProperty(email));
        driver.findElement(By.id(loc.getProperty("contactName_field"))).sendKeys(input.getProperty(name));
        driver.findElement(By.id(loc.getProperty("message_field"))).sendKeys(input.getProperty(message));

        driver.findElement(By.xpath(loc.getProperty("send_message"))).click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        System.out.println(alertText);

        Assert.assertEquals(alertText, expectedAlertMessage);
        alert.accept();
    }

    @Test(description = "Validate sending a message after clicking the 'Contact' header option")
    public static void TC_01() {
        sendMessage("contact_email", "contact_name", "message", loc.getProperty("alert_message"));
    }

    // It doesn't give an error message - REPORT DEFECT!!!
    @Test(description = "Validate sending a message without filling the 'Message field'", priority = 1)
    public static void TC_02() {
        String expectedAlert = "Please fill out the 'Message' field";
        sendMessage("contact_email", "contact_name", "", expectedAlert);
    }

    // It doesn't give an error message - REPORT DEFECT!!!
    @Test(description = "Validate sending a message without filling any of the fields", priority = 2)
    public static void TC_03() {
        String expectedAlert = "Please fill out the fields";
        sendMessage("", "", "", expectedAlert);
    }

    @Test(description = "Validate closing the 'Contact' window", priority = 3)
    public static void TC_04() {
        driver.findElement(By.linkText(loc.getProperty("contact_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(loc.getProperty("contactEmail_field"))));

        driver.findElement(By.xpath(loc.getProperty("contact_close"))).click();

        boolean isWindowClosed =
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(loc.getProperty("contact_window"))));
        Assert.assertTrue(isWindowClosed, "Window is no longer visible");
    }
}