package test_scenarios;

import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TS09AboutUsTest extends TestBase {
    @Test(description = "Validate that the correct video is displayed when you click the 'About us' header link")
    public static void TC_01() {
        driver.findElement(By.linkText(loc.getProperty("aboutUs_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("video_window"))));

        WebElement video = driver.findElement(By.xpath(loc.getProperty("video_window")));
        String videoSrc = video.getAttribute("src");
        Assert.assertTrue(videoSrc.contains(loc.getProperty("video_src")));
    }

    @Test(description = "Validate if the video can be played", priority = 1)
    public static void TC_02() throws InterruptedException {
        driver.findElement(By.linkText(loc.getProperty("aboutUs_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("video_button"))));
        driver.findElement(By.xpath(loc.getProperty("video_button"))).click();

        Thread.sleep(2000);

        WebElement videoStarted = driver.findElement(By.xpath(loc.getProperty("video")));
        String isVideoStarted = videoStarted.getAttribute("class");
        Assert.assertTrue(isVideoStarted.contains(loc.getProperty("video_start")));
    }

    @Test(description = "Validate closing the 'About us' window", priority = 2)
    public static void TC_03() {
        driver.findElement(By.linkText(loc.getProperty("aboutUs_link"))).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc.getProperty("video_window"))));

        driver.findElement(By.xpath(loc.getProperty("closeAboutUs_window"))).click();

        boolean isWindowClosed =
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc.getProperty("video_window"))));
        Assert.assertTrue(isWindowClosed, "Window is no longer visible");
    }
}