package test_scenarios;

import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TS04ProductDisplayPageTest extends TestBase {

    @Test(description = "Validate that the proper product names are displayed after clicking on a product")
    public static void TC_01() {
        List<String> productNames = Arrays.asList("samsung_S6", "nokia_lumia", "nexus");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        productNames.forEach(productName -> {
            driver.findElement(By.linkText(loc.getProperty(productName))).click();

            WebElement productHeader = driver.findElement(By.className(loc.getProperty("product_header")));
            Assert.assertEquals(productHeader.getText(), loc.getProperty(productName));

            driver.navigate().back();
        });
    }

    @Test(description = "Validate that the proper prices are displayed under each product", priority = 1)
    public static void TC_02() {
        Map<String, String> products = new HashMap<>();
        products.put("samsung_S6", loc.getProperty("samsung_price"));
        products.put("nokia_lumia", loc.getProperty("nokia_price"));
        products.put("nexus", loc.getProperty("nexus_price"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        products.forEach((productName, expectedPrice) -> {

            driver.findElement(By.linkText(loc.getProperty(productName))).click();

            WebElement price = driver.findElement(By.className(loc.getProperty("price_locator")));
            Assert.assertEquals(price.getText(), expectedPrice);

            driver.navigate().back();
        });
    }

    @Test(description = "Validate that the correct thumbnail photo of each product is displayed", priority = 2)
    public static void TC_03() {
        Map<String, String> products = new HashMap<>();
        products.put("samsung_S6", loc.getProperty("samsung_thumbnail"));
        products.put("nokia_lumia", loc.getProperty("nokia_thumbnail"));
        products.put("nexus", loc.getProperty("nexus_thumbnail"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        products.forEach((productName, expectedThumbnail) -> {
            driver.findElement(By.linkText(loc.getProperty(productName))).click();

            WebElement thumbnail = driver.findElement(By.xpath(loc.getProperty("thumbnail_locator")));
            Assert.assertEquals(thumbnail.getAttribute("src"), expectedThumbnail);

            driver.navigate().back();
        });
    }

    @Test(description = "Validate that the proper description is displayed under each product", priority = 3)
    public static void TC_04() {
        Map<String, String> products = new HashMap<>();
        products.put("samsung_S6", loc.getProperty("samsung_description"));
        products.put("nokia_lumia", loc.getProperty("nokia_description"));
        products.put("nexus", loc.getProperty("nexus_description"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        products.forEach((productName, expectedDescription) -> {
            driver.findElement(By.linkText(loc.getProperty(productName))).click();

            WebElement description = driver.findElement(By.xpath(loc.getProperty("description_locator")));
            Assert.assertEquals(description.getText(), expectedDescription);

            driver.navigate().back();
        });
    }
}