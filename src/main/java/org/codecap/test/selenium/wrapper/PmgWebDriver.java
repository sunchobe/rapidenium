package org.codecap.test.selenium.wrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Diese Klasse stellt einen einfachen Wrapper für den WebDriver dar.
 */
public class PmgWebDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmgWebDriver.class);

    protected WebDriver webDriver;

    private int xCoordinate = 0;

    private int yCoordinate = 0;

    public PmgWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        webDriver.manage().window().maximize();
    }

    public PmgWebDriver(PmgWebDriver pageInformation) {
        this(pageInformation.webDriver);
    }

    public void get(String url) {
        webDriver.get(url);
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public void close() {
        webDriver.close();
    }

    public void quit() {
        webDriver.quit();
    }

    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    public Navigation navigate() {
        return webDriver.navigate();
    }

    public Options manage() {
        return webDriver.manage();
    }

    public void scrollX(int distance) {
        if ((xCoordinate + distance) < 0) {
            LOGGER.warn("X-value after scrolling would be negative. Resetting to '0'.");
            xCoordinate = 0;
        } else {
            xCoordinate += distance;
        }
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("javascript:window.scroll(" + xCoordinate + "," + yCoordinate + ")");
    }

    public void scrollY(int distance) {
        if ((yCoordinate + distance) < 0) {
            LOGGER.warn("Y-value after scrolling would be negative. Resetting to '0'.");
            yCoordinate = 0;
        } else {
            yCoordinate += distance;
        }
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("javascript:window.scroll(" + xCoordinate + "," + yCoordinate + ")");

    }

    public void scrollScreenDown() {
        scrollY(getCurrentScreenHeight());
    }

    public void scrollScreenUp() {
        scrollY(-getCurrentScreenHeight());
    }

    public void scrollScreenLeft() {
        scrollX(-getCurrentScreenWidth());
    }

    public void scrollScreenRight() {
        scrollX(getCurrentScreenWidth());
    }

    private int getCurrentScreenWidth() {
        return getWindowDimension().getWidth();
    }

    private int getCurrentScreenHeight() {
        return getWindowDimension().getHeight();
    }

    private Dimension getWindowDimension() {
        Dimension dimension = webDriver.manage().window().getSize();
        LOGGER.debug("Found following dimension for Window: ({}|{})", new Object[]{Integer.valueOf(dimension.getWidth()), Integer.valueOf(dimension.getHeight())});
        return dimension;
    }
}
