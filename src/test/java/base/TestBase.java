package base;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Listeners(utilities.Listeners.class)
public class TestBase {
    public static WebDriver driver;
    public static Properties prop = new Properties();
    public static Properties loc = new Properties();
    public static Properties input = new Properties();
    public static FileReader frProp;
    public static FileReader frLoc;
    public static FileReader frIn;

    @BeforeMethod
    public void setUp() throws IOException {
        String userDirectory = System.getProperty("user.dir");
        if (driver == null) {
            frProp = new FileReader(userDirectory + "\\src\\test\\resources\\config_files\\config.properties");
            frLoc = new FileReader(userDirectory + "\\src\\test\\resources\\config_files\\locators.properties");
            frIn = new FileReader(userDirectory + "\\src\\test\\resources\\config_files\\input.properties");

            prop.load(frProp);
            loc.load(frLoc);
            input.load(frIn);
        }
        if (prop.getProperty("browser").equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (prop.getProperty("browser").equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (prop.getProperty("browser").equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        driver.get(prop.getProperty("testUrl"));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    public static void LogIn() {
        driver.findElement(By.linkText(loc.getProperty("logIn_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        WebElement signUpWindow = wait.until(visibilityOfElementLocated(By.xpath(loc.getProperty("logIn_window"))));

        if (signUpWindow.isDisplayed()) {
            System.out.println("Window is visible");
        } else {
            System.out.println("Window is not visible");
        }

        driver.findElement(By.id(loc.getProperty("log_User"))).sendKeys(input.getProperty("user"));
        driver.findElement(By.id(loc.getProperty("log_pass"))).sendKeys(input.getProperty("pass"));
        driver.findElement(By.xpath(loc.getProperty("logIn_button"))).click();

        WebElement welcome = wait.until(visibilityOfElementLocated(By.xpath(loc.getProperty("welcome_header"))));
        Assert.assertTrue(welcome.isDisplayed(), "Element is visible");

        String actualText = welcome.getText();
        String expectedText = "Welcome " + input.getProperty("user");
        Assert.assertEquals(actualText, expectedText, "Element text is as expected");
    }

    public static void AddToCart() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.linkText(loc.getProperty("samsung_S6"))).click();
        driver.findElement(By.linkText(loc.getProperty("addToCart_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        if (alertText.contains(".")) {
            Assert.assertEquals(alertText, loc.getProperty("added_alert"));
        } else {
            Assert.assertEquals(alertText, loc.getProperty("added_alert").replace(".", ""));
        }
        alert.accept();

        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();
        WebElement titleInCart = driver.findElement(By.xpath(loc.getProperty("title_locator")));
        Assert.assertEquals(titleInCart.getText(), loc.getProperty("samsung_S6"));
    }

    public static void AddingMultipleProductsToCart() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        String[] products = {"samsung_S6", "nokia_lumia", "nexus"};

        Arrays.stream(products)
                .forEach(product -> {
                    driver.findElement(By.linkText(loc.getProperty(product))).click();
                    driver.findElement(By.linkText(loc.getProperty("addToCart_link"))).click();

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                    wait.until(alertIsPresent());

                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    Assert.assertEquals(alertText, loc.getProperty("added_alert").replace(".", ""));
                    alert.accept();

                    driver.navigate().back();
                    driver.navigate().back();
                });

        driver.findElement(By.linkText(loc.getProperty("cart_link"))).click();

        String[] expectedTexts = {loc.getProperty("nokia_lumia"),
                loc.getProperty("samsung_S6"),
                loc.getProperty("nexus")};

        for (int i = 1; i <= 3; i++) {
            String xpath = "//*[@id='tbodyid']/tr[" + i + "]/td[2]";
            WebElement titleName = driver.findElement(By.xpath(xpath));
            String TitleText = titleName.getText();

            boolean found = Arrays.stream(expectedTexts).anyMatch(TitleText::contains);
            String message = "Element text for row " + i + " does not contain any of the expected texts";
            Assert.assertTrue(found, message);
        }
    }

    public static void PlaceOrder() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        driver.findElement(By.xpath(loc.getProperty("placeOrder_link"))).click();

        String[] fieldIds =
                {"orderName_field", "orderCountry_field", "orderCity_field", "orderCard_field",
                        "orderMonth_field", "orderYear_field"};
        String[] inputData = {"order_name", "order_county", "order_city", "order_card", "order_month", "order_year"};

        for (int i = 0; i < fieldIds.length; i++) {
            driver.findElement(By.id(loc.getProperty(fieldIds[i]))).sendKeys(input.getProperty(inputData[i]));
        }

        driver.findElement(By.xpath(loc.getProperty("purchase_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(visibilityOfElementLocated(By.xpath(loc.getProperty("placeOrder_window"))));

        WebElement purchaseSuccess = driver.findElement(By.xpath(loc.getProperty("purchase_success")));
        String purchaseSuccessText = purchaseSuccess.getText();

        Assert.assertEquals(purchaseSuccessText, loc.getProperty("purchaseSuccess_text"));
    }
}