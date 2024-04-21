package test_scenarios;

import base.base;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TS05CartFunctionalityTest extends base {

    public static void EmptyCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc.getProperty("title_locator"))));

        boolean isProductInCart = driver.findElements(By.xpath(loc.getProperty("title_locator"))).isEmpty();
        Assert.assertTrue(isProductInCart, "Cart is not empty");
    }

    @Test(description = "Validate adding the product to Cart from 'Product Display' Page")
    public static void TC_01() {
        AddToCart();
    }

    @Test(description = "Validate adding the product to Cart from 'Product Display' Page after logging in",
            priority = 1)
    public static void TC_02() {
        LogIn();

        AddToCart();
    }

    @Test(description = "Validate deleting a product after adding it to cart", priority = 2)
    public static void TC_03() {
        AddToCart();

        driver.findElement(By.linkText(loc.getProperty("deleteProduct_link"))).click();

        EmptyCart();
    }

    // Have to click back twice - Report!
    @Test(description = "Validate adding multiple products to cart", priority = 3)
    public static void TC_04() {
        AddingMultipleProductsToCart();

    }

    @Test(description = "Validate the cart status when there are no products added", priority = 4)
    public static void TC_05() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();

        EmptyCart();
    }
}