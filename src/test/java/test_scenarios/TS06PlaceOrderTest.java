package test_scenarios;

import base.TestBase;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TS06PlaceOrderTest extends TestBase {

    @Test(description = "Validate placing an order after adding a product to cart")
    public static void TC_01() {
        AddToCart();

        PlaceOrder();
    }

    @Test(description = "Validate logging in and placing an order after adding a product to cart", priority = 1)
    public static void TC_02() {
        LogIn();

        AddToCart();

        PlaceOrder();
    }

    // Order can be placed even without any products - REPORT DEFECT!!!
    @Test(description = "Validate placing an order without adding any product to cart", priority = 2)
    public static void TC_03() {
        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();

        PlaceOrder();
    }

    @Test(description = "Validate placing an order after adding multiple products to cart", priority = 3)
    public static void TC_04() {
        AddingMultipleProductsToCart();

        PlaceOrder();
    }

    @Test(description = "Validate placing an order without filling any of the fields", priority = 4)
    public static void TC_05() {
        AddToCart();

        driver.findElement(By.xpath(loc.getProperty("placeOrder_link"))).click();
        driver.findElement(By.xpath(loc.getProperty("purchase_link"))).click();

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        wait.until(alertIsPresent());

        Assert.assertEquals(alertText, loc.getProperty("alert_emptyFields"));
    }

    // Order can still be placed - REPORT DEFECT!!!
    @Test(description = "Validate placing an order with an invalid date", priority = 5)
    public static void TC_06() {
        AddToCart();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.xpath(loc.getProperty("placeOrder_link"))).click();

        String[] fieldIds =
                {"orderName_field", "orderCountry_field", "orderCity_field", "orderCard_field",
                        "orderMonth_field", "orderYear_field"};
        String[] inputData = {"order_name", "order_county", "order_city", "order_card", "invalid_month",
                "invalid_year"};

        for (int i = 0; i < fieldIds.length; i++) {
            driver.findElement(By.id(loc.getProperty(fieldIds[i]))).sendKeys(input.getProperty(inputData[i]));
        }

        driver.findElement(By.xpath(loc.getProperty("purchase_link"))).click();
    }

    // Order can still be placed - REPORT DEFECT!!!
    @Test(description = "Validate placing an order with an invalid card number", priority = 6)
    public static void TC_07() {
        AddToCart();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.xpath(loc.getProperty("placeOrder_link"))).click();

        String[] fieldIds =
                {"orderName_field", "orderCountry_field", "orderCity_field", "orderCard_field",
                        "orderMonth_field", "orderYear_field"};
        String[] inputData = {"order_name", "order_county", "order_city", "order_name", "invalid_month",
                "invalid_year"};

        for (int i = 0; i < fieldIds.length; i++) {
            driver.findElement(By.id(loc.getProperty(fieldIds[i]))).sendKeys(input.getProperty(inputData[i]));
        }

        driver.findElement(By.xpath(loc.getProperty("purchase_link"))).click();
    }

    @Test(description = "Validate closing the 'Place order' window", priority = 7)
    public static void TC_08() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();

        driver.findElement(By.xpath(loc.getProperty("placeOrder_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(visibilityOfElementLocated(By.xpath(loc.getProperty("closeOrder_button"))));

        WebElement closeButton = driver.findElement(By.xpath(loc.getProperty("closeOrder_button")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", closeButton);

        driver.findElement(By.xpath(loc.getProperty("closeOrder_button"))).click();

        wait.until(invisibilityOfElementLocated(By.xpath(loc.getProperty("placeOrder_window"))));
    }
}