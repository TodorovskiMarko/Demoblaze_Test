package test_scenarios;

import base.base;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TS03LogoutFunctionalityTest extends base {

    public static void LogOut() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement logOut = wait.until(visibilityOfElementLocated(By.linkText(loc.getProperty("logOut_link"))));
        logOut.click();

        WebElement logIn = wait.until(visibilityOfElementLocated(By.linkText(loc.getProperty("logIn_link"))));
        Assert.assertTrue(logIn.isDisplayed(), "Element is visible");
    }

    public static void LogOutVisibility() {
        // Check if the element is not present
        boolean isElementPresent = false;
        try {
            driver.findElement(By.linkText("logOut_link"));
            isElementPresent = true; // Element is present
        } catch (NoSuchElementException e) {
            // Element is not present
        }

        Assert.assertFalse(isElementPresent, "Log out link is not displayed");
    }

    @Test(description = "Validate Logging out by selecting Log out header option")
    public static void TC_01() {
        LogIn();

        LogOut();
    }

    @Test(description = "Validate logging out and browsing back", priority = 1)
    public static void TC_02() {
        LogIn();
        LogOut();

        driver.navigate().back();

        LogOutVisibility();
    }

    @Test(description = "Validate logging out and logging in immediately after logout ", priority = 2)
    public static void TC_03() {
        LogIn();

        LogOut();

        LogIn();
    }

    @Test(description = "Validate Log out is not displayed before logging in", priority = 3)
    public static void TC_04() {
        LogOutVisibility();
    }
}