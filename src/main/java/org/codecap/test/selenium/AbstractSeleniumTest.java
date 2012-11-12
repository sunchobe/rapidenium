package org.codecap.test.selenium;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codecap.test.selenium.wrapper.PmgWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Diese Klasse stellt die Basisklasse für alle PMG-Tests dar.
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/selenium-configuration.xml" })
public abstract class AbstractSeleniumTest {

    private static final Logger LOGGER = Logger.getLogger(AbstractSeleniumTest.class);

    @Rule
    public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule();

    @Resource(name = "defaultSeleniumConfiguration")
    private SeleniumConfiguration seleniumConfiguration;

    private WebDriver webDriver;

    /** Hier werden die Basisinformationen für die jeweilige Testklasse initialisiert. */
    @Before
    public void prepare() {
        if (webDriver == null) {
            webDriver = new RemoteWebDriver(seleniumConfiguration.getSeleniumServerUrl(), getCapabilities(seleniumConfiguration));
        }
    }

    /** Der Shutdown, nachdem alle Tests einer Klasse gelaufen sind. */
    @After
    public void finish() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    private Capabilities getCapabilities( SeleniumConfiguration seleniumConfiguration ) {
        switch ( seleniumConfiguration.getBrowser() ) {
        case FIREFOX:
            return DesiredCapabilities.firefox();
        case INTERNET_EXPLORER:
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            return ieCapabilities;
        default:
            throw new RuntimeException("Unsupported browser type='" + seleniumConfiguration.getBrowser() + "' requested.");
        }
    }

    /**
     * Browse-Methode, um eine bestimmte URL anzusurfen.
     *
     * @param resource Die Resource, welche angedurft werden soll.
     * @return Ein Objekt, welches den Inahlt der ermittelten Resource darstellt.
     */
    protected final PmgWebDriver goToPage( String resource ) {

        LOGGER.info("requesting resource '" + resource + "' at '" + seleniumConfiguration.getApplicationRootUrl() + getBaseResourcePath() + "'");

        webDriver.get(seleniumConfiguration.getApplicationRootUrl() + getBaseResourcePath() + resource);

        return new PmgWebDriver(webDriver);
    }

    protected abstract String getBaseResourcePath();

	class ScreenshotTestRule implements MethodRule {

        private static final String TARGET_DIRECTORY_SELENIUM_SCREENSHOTS = "target/surefire-reports/selenium-screenshots/";

        public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        statement.evaluate();
                    } catch (Throwable t) {
                        captureScreenshot(frameworkMethod.getName());
                        throw t; // rethrow to allow the failure to be reported to JUnit
                    }
                }

                public void captureScreenshot(String methodName) {
                    try {
                        String directoryName = TARGET_DIRECTORY_SELENIUM_SCREENSHOTS + AbstractSeleniumTest.this.getClass().getSimpleName() + File.separator;
                        new File(directoryName).mkdirs();

                        String fileName = directoryName + methodName + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date()) + ".png";

                        FileOutputStream out = new FileOutputStream(fileName);

                        WebDriver augmentedDriver = new Augmenter().augment(webDriver);
                        TakesScreenshot takesScreenshot = (TakesScreenshot) augmentedDriver;

                        out.write(takesScreenshot.getScreenshotAs(OutputType.BYTES));
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        LOGGER.error("An Exception occured while taking Screenshot.", e);
                    }
                }
            };
        }
    }
}
