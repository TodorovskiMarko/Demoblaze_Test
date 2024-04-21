package test_scenarios;

import base.base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class TS07HomePageTest extends base {

    public static void HomePage() {
        WebElement products = driver.findElement(By.id(loc.getProperty("homePage_products")));
        Assert.assertTrue(products.isDisplayed(), "Element is not visible");
    }

    static int OneSecWait = 1000;

    public static void DisplayedProductsInCategory
            (String categoryLinkText, String[] products) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.linkText(loc.getProperty(categoryLinkText))).click();
        Thread.sleep(OneSecWait);

        List<WebElement> elements = driver.findElements(By.className(loc.getProperty("products_className")));

        for (int i = 0; i < products.length; i++) {
            WebElement element = elements.get(i);
            String elementText = element.getText();
            Assert.assertEquals(elementText, loc.getProperty(products[i]), "Product is not displayed");
        }
    }

    private static void ValidateNextAndPrevious
            (String[] expectedProducts, String buttonLocator) throws InterruptedException {
        Thread.sleep(OneSecWait);
        driver.findElement(By.xpath(loc.getProperty(buttonLocator))).click();
        Thread.sleep(OneSecWait);
        List<WebElement> elements = driver.findElements(By.className(loc.getProperty("products_className")));

        for (int i = 0; i < expectedProducts.length; i++) {
            WebElement element = elements.get(i);
            String elementText = element.getText();
            Assert.assertEquals(elementText, loc.getProperty(expectedProducts[i]), "Product is not displayed");
        }
    }

    @Test(description = "Validate navigating to Home Page from the Cart page")
    public static void TC_01() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();
        driver.findElement(By.xpath(loc.getProperty("home_link"))).click();

        HomePage();
    }

    @Test(description = "Validate navigating to Home Page after placing an order", priority = 1)
    public static void TC_02() {
        AddToCart();
        PlaceOrder();

        driver.findElement(By.xpath("//button[normalize-space()='OK']")).click();

        HomePage();
    }

    @Test(description = "Validate navigating to Home Page from the 'Product Display' page", priority = 2)
    public static void TC_03() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.linkText(loc.getProperty("samsung_S6"))).click();
        driver.findElement(By.xpath(loc.getProperty("home_link"))).click();

        HomePage();
    }

    @Test(description = "Validate navigating to Home Page from any page of the application using the logo",
            priority = 3)
    public static void TC_04() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();
        driver.findElement(By.id(loc.getProperty("application_logo"))).click();

        HomePage();
    }

    @Test(description = "Validate Hero Images and its slider options in the Home Page", priority = 4)
    public static void TC_05() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.xpath(loc.getProperty("nextSlider_button"))).click();
        WebElement SecondPhotoThumbnail = driver.findElement(By.xpath(loc.getProperty("second_slide")));
        String thumbnailSrc = SecondPhotoThumbnail.getAttribute("src");

        Assert.assertEquals(thumbnailSrc, loc.getProperty("nexus_photo"), "Incorrect image source");

        driver.findElement(By.xpath(loc.getProperty("prevSlider_button"))).click();
        WebElement thumbnails = driver.findElement(By.xpath(loc.getProperty("first_slide")));
        String thumbnailSrc1 = thumbnails.getAttribute("src");

        Assert.assertEquals(thumbnailSrc1, loc.getProperty("samsung_photo"), "Incorrect image source");
    }

    @Test(description = "Validate nine products should be featured on the Home Page", priority = 5)
    public static void TC_06() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        List<WebElement> products = driver.findElements(By.xpath(loc.getProperty("products_displayed")));
        int actualLength = products.size();
        int expectedLength = 9;

        Assert.assertEquals(actualLength, expectedLength, "Incorrect length of class");
    }

    @Test(description = "Validate that only phones are displayed after clicking the 'Phones' category", priority = 6)
    public static void TC_07() throws InterruptedException {
        String[] products = {"samsung_S6", "nokia_lumia", "nexus", "samsung_S7", "iphone_6", "sony_xperia", "htc"};
        DisplayedProductsInCategory("phonesCategory_link", products);
    }

    @Test(description = "Validate that only laptops are displayed after clicking the 'Laptops' category", priority = 7)
    public static void TC_08() throws InterruptedException {
        String[] products = {"sonyVaio_i5", "sonyVaio_i7", "macBook_air", "dell_i7", "2017_dell", "macBook_Pro"};
        DisplayedProductsInCategory("laptopsCategory_link", products);
    }

    @Test(description = "Validate that only monitors are displayed after clicking the 'Monitors' category",
            priority = 8)
    public static void TC_09() throws InterruptedException {
        String[] products = {"apple_monitor", "asus"};
        DisplayedProductsInCategory("monitorsCategory_link", products);
    }

    @Test(description = "Validate the workings of 'Next' and 'Previous' buttons", priority = 9)
    public static void TC_10() throws InterruptedException {
        String[] productsNext = {"apple_monitor", "macBook_air", "dell_i7", "2017_dell", "asus", "macBook_Pro"};
        String[] productsPrev =
                {"nokia_lumia", "nexus", "samsung_S7", "iphone_6", "sony_xperia", "htc", "sonyVaio_i5",
                        "sonyVaio_i7", "apple_monitor"};

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        ValidateNextAndPrevious(productsNext, "next_button");
        ValidateNextAndPrevious(productsPrev, "previous_button");
    }
}