package org.codecap.test.selenium.element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codecap.test.selenium.SeleniumException;
import org.codecap.test.selenium.wrapper.PmgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Diese Klasse erweitert {@link PmgWebDriver} um die Methoden und Attribute, um Seitenelemente komfortabel
 * zu definieren und automatisch zu ermitteln.
 */
public abstract class Page extends PmgWebDriver {

    private static final Logger LOGGER = Logger.getLogger(Page.class);

    private final Map<AccessType, Map<String, WebElement>> webElementMap;

    /**
     * Konstruktor, welcher eine {@link PmgWebDriver} entgegennimmt, um sich zu initialisieren. Des Weiteren werden
     * die initialen Element-Maps initialisiert und mit den Werten befüllt, welche in den Unterklassen initialisiert und
     * instantiiert werden müssen.
     *
     * @param pageInformation Bei der Instantiierung von Seiten wird immer eine Referenzseite benötigt, welche den zentralen {@link org.openqa.selenium.WebDriver}
     * mit sich führt, um an die aktuellen Seiteninhalten heranzukommen.
     * @throws SeleniumException Wenn bei der Initialisierung der Seite (Ermittlung der definierten Seitenelemente: {@link Page#retrieveDefinedPageElements()})
     *                           ein Fehler auftritt.
     */
    public Page(PmgWebDriver pageInformation) {
        super(pageInformation);

        this.webElementMap = new HashMap<AccessType, Map<String, WebElement>>();
        for (AccessType accessType : AccessType.values()) {
            this.webElementMap.put(accessType, new HashMap<String, WebElement>());
        }

        definePageElements();

        retrieveDefinedPageElements();
    }

    /**
     * Initialisiert alle Seitenelemente, welche in {@link Page#getDefinedPageElements()} definiert wurden.
     */
    private void retrieveDefinedPageElements() {
        for (PageElement pageElement : getDefinedPageElements()) {
            Map<String, WebElement> typedMap;
            try {
                typedMap = this.webElementMap.get(pageElement.getAccessType());
            } catch (NullPointerException e) {
                throw new SeleniumException(
                        "Could not retrieve PageElement. Maybe it is not initialized. Please check if all defined PageElements are initialized in Page.definePageElements().");
            }
            if (typedMap.containsKey(pageElement.getKey())) {
                LOGGER.error("requested Element '" + pageElement + "' already exists in the map of retrieved elements. Current Value is " + typedMap.get(pageElement.getKey()));
            } else {
                typedMap.put(pageElement.getKey(), getWebElement(pageElement, 0));
            }
        }
    }

    /**
     * Ermittelt das gesuchte Seitenelement. Sollte es nicht auf Anhieb funktionieren, wird versucht es {@link Page#EXCEPTION_THRESHOLD}-mal zu ermitteln. Zwischen den Versuchen
     * wird
     * jeweils
     * {@link Page#EXCEPTION_SLEEP} Millisekunden gewartet.
     *
     * @param pageElement Das Element, welches ermittelt werden soll.
     * @param exceptionCount Die aktuelle Anzahl von Versuchen.
     * @return Das ermittelte Element.
     * @throws SeleniumException Wenn das Element nicht gefunden werden konnte.
     */
    private WebElement getWebElement(PageElement pageElement, int exceptionCount) {

        try {
            switch (pageElement.getAccessType()) {
            case ID:
                if (pageElement.hasFrame()) {
                    return enter(pageElement.getFrame()).findElement(By.id(pageElement.getKey()));
                }
                return this.findElement(By.id(pageElement.getKey()));
            case NAME:
                if (pageElement.hasFrame()) {
                    return enter(pageElement.getFrame()).findElement(By.name(pageElement.getKey()));
                }
                return this.findElement(By.name(pageElement.getKey()));
            case XPATH:
                if (pageElement.hasFrame()) {
                    return enter(pageElement.getFrame()).findElement(By.xpath(pageElement.getKey()));
                }
                return this.findElement(By.xpath(pageElement.getKey()));
            case FRAME:
                return this.findElement(By.id(pageElement.getKey()));
//            case SELENIUM_ID:
//
//                (new PmgWebDriverWait<Page>(this, 10)).until(new PmgExpectedPageCondition<Boolean, Page>(this) {
//                    public Boolean apply(Page page) {
//                        return Boolean.valueOf(page.getPageSource().endsWith("</html>"));
//                    }
//                });
//
//                return this.findElement(By.xpath(".//*[@seleniumId = '" + pageElement.getKey() + "']"));
            default:
                throw new SeleniumException("Could not get WebElement for " + pageElement);
            }
        } catch (NoSuchElementException e) {
            throw new SeleniumException("Could not retrieve element: " + pageElement + " from " + getPageSource(), e);
        }
    }

    /**
     * Zugriffsmethode, welche von den Unterklassen verwendet wird, um auf definierte Seiteninhalte zugreifen zu können.
     *
     * @param pageElement Das definierte und initialisierte Seitenelement.
     * @return Das WebElement, auf welchem Aktionen ausgeführt werden können.
     */
    private WebElement get(PageElement pageElement) {
        Map<String, WebElement> accessTypeWebElementMap = this.webElementMap.get(pageElement.getAccessType());
        if (accessTypeWebElementMap != null) {
            if (accessTypeWebElementMap.containsKey(pageElement.getKey())) {
                return accessTypeWebElementMap.get(pageElement.getKey());
            } else {
                throw new SeleniumException("Could not retrieve WebElement '" + pageElement + "' from page content " + this.getPageSource() + ".");
            }
        } else {
            throw new SeleniumException("Could not retrieve Map containing WebElements for AccessType " + pageElement.getAccessType());
        }
    }

    public boolean isVisible(PageElement pageElement) {
        if (pageElement != null) {
            return !get(pageElement).getCssValue("display").equals("none");
        } else {
            return false;
        }
    }

    public boolean isClass(PageElement pageElement, String expectedClass) {
        String classString = get(pageElement).getAttribute("class");

        boolean isClass = false;
        isClass |= classString.equals(expectedClass); // equals
        isClass |= classString.startsWith(expectedClass + " "); // startswith -> etwaige weitere klassen folgen
        isClass |= classString.endsWith(" " + expectedClass); // endswith -> etwaige weitere klassen vorher
        isClass |= classString.contains(" " + expectedClass + " "); // endswith -> gesuchte klasse von anderen umrahmt 

        return isClass;
    }

    public boolean textMatches(PageElement pageElement, String regexp) {
        final String text = get(pageElement).getText();
        final boolean success;
        if (regexp != null) {
            final Pattern pattern = Pattern.compile(regexp, Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(text);
            success = text != null && matcher.matches();
        } else {
            success = text == null;
        }
        if (!success) {
            LOGGER.error("PageElement '" + pageElement.toString() + "' text content '" + text + "' " + "doesn't match regexp '" + regexp + "'.");
        }
        return success;
    }

    public boolean isLinkTo(PageElement pageElement, String hrefRegExp) {
        final WebElement webElement = get(pageElement);
        final String tagName = webElement.getTagName();

        boolean success = tagName.equalsIgnoreCase("a");
        if (!success) {
            LOGGER.error("PageElement '" + pageElement.toString() + "' was expected to be a <a>-Tag but is actually a <" + tagName + ">-Tag.");
        }

        final String href = webElement.getAttribute("href");
        if (href == null) {
            success = false;
            LOGGER.error("PageElement '" + pageElement.toString() + "' href attribute is missing.");
        } else {
            final Pattern pattern = Pattern.compile(hrefRegExp, Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(href);
            success &= matcher.matches();
            if (!success) {
                LOGGER.error("PageElement '" + pageElement.toString() + "' href attribute '" + href + "' " + "doesn't match regexp '" + hrefRegExp + "'.");
            }
        }
        return success;
    }

    public boolean pageSourceMatches(String regExp) {
        final String pageSource = getPageSource();
        final Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(pageSource);
        boolean isValid = matcher.matches();
        if (!isValid) {
            LOGGER.error("Page source doesn't match regexp '" + regExp + "':\n" + pageSource);
        }
        return isValid;
    }

    /**
     * Methode, um Frames zu betreten und auf deren Inhalte auch zugreifen zu können.
     *
     * @param frame Der definierte Frame, sofern vorhanden.
     * @return Die ermittelte Seiteninformationen mit den entsprechenden Inhalten.
     */
    protected PmgWebDriver enter(Frame frame) {
        // zum basisframe zurückkehren
        this.switchTo().defaultContent();
        // den eigentlichen Frame ansteuern
        return new PmgWebDriver(this.switchTo().frame(frame.getKey()));
    }

    /**
     * Hier kann man komfortabel in ein definiertes Feld einen Wert eingeben.
     *
     * @param input Die Eingabe, welcher erfolgen soll.
     * @param pageElement Das Element, welches die Eingabe empfangen soll.
     */
    protected void input(String input, PageElement pageElement) {
        get(pageElement).sendKeys(input);
    }

    protected void click(PageElement pageElement) {
        get(pageElement).click();
    }

    public String getAttribute(PageElement pageElement, String attributeName) {
        return get(pageElement).getAttribute(attributeName);
    }

    /**
     * Hier werden alle zu definierenden {@link PageElement}e initialisiert und instantiiert. {@link PageElement}e, welche hier nicht initialisiert werden, sind nicht zugreifbar
     * und
     * wird beim Zugriff
     * durch {@link #retrieveDefinedPageElements()} (z.B. im Konstruktor) eine SeleniumException geworfen.
     */
    protected abstract void definePageElements();

    /**
     * Gibt die Liste der definierten Elemente zurück, welche auf der Seite zur Verfügung stehen sollten und verwendet werden können.
     *
     * @return Die Liste der {@link PageElement}e.
     */
    protected abstract List<PageElement> getDefinedPageElements();

    /**
     * Hier werden die notwendigen Validierungen vorgenommen, um sicherzustellen, dass die Seite auch die erwartete Seite ist.
     *
     * @return <code>true</code>, wenn die Seite alle Validierungen besteht, <code>false</code>, sonst.
     */
    public abstract boolean isValid();

}
