package org.codecap.test.selenium;

import java.net.URL;

import org.codecap.test.selenium.SeleniumUrl.SeleniumProtocolType;

/**
 * Diese Klasse ist ein Bean, welches die Konfigurationsparameter für Selenium
 * enthält.
 */
public class SeleniumConfiguration {

	private static final String PROPERTY_KEY_SELENIUM_SERVER_RESOURCE = "selenium.server.resource";

	/**
	 * Port des Selenium Servers. Beispiel: 4444
	 */
	private static final String PROPERTY_KEY_SELENIUM_SERVER_PORT = "selenium.server.port";

	private static final String PROPERTY_KEY_SELENIUM_SERVER_PROTOCOL = "selenium.server.protocol";

	/**
	 * Hostname des Selenium Servers. Beispiel: localhost
	 */
	private static final String PROPERTY_KEY_SELENIUM_SERVER_HOSTNAME = "selenium.server.hostname";

	/**
	 * Port der zu testenden Applikation. Beispiel: 8080
	 */
	private static final String PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_PORT = "selenium.application.root.port";

	/**
	 * Protokoll der zu testenden Applikation. Üblicherweise 'http'
	 */
	private static final String PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_PROTOCOL = "selenium.application.root.protocol";

	/**
	 * Root hostname der zu testenden Applikation. Beispiel: 'www.google.de'
	 */
	private static final String PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_HOSTNAME = "selenium.application.root.hostname";

	/**
	 * Der Browser der beim testn zum Einsatz kommen soll. Aktuell unterstützte
	 * Wert sind: 'FRIEFOX', 'INTERNET_EXPLORER'.
	 */
	private static final String PROPERTY_KEY_SELENIUM_BROWSER = "selenium.browser";

	private URL seleniumServerUrl;

	private URL applicationRootUrl;

	private SeleniumBrowserType browser;

	/**
	 * Konstruktor, welche die Mindestanforderungen der Seleniumkonfiguration
	 * entgegennimmt.
	 * 
	 * @param seleniumServerUrl
	 *            Die Server-Rul der Selenium-Instanz.
	 * @param applicationRootUrl
	 *            Die Root-Url der Applikation.
	 * @param browser
	 *            Der Browser, welcher gestartet werden soll.
	 */
	public SeleniumConfiguration(SeleniumUrl seleniumServerUrl, SeleniumUrl applicationRootUrl, SeleniumBrowserType browser) {
		super();

		initializeSeleniumServerUrl(seleniumServerUrl);
		initializeSeleniumApplicationRootUrl(applicationRootUrl);
		initializeSeleniumBrowser(browser);

	}

	private void initializeSeleniumBrowser(SeleniumBrowserType browser) {
		this.browser = SeleniumBrowserType.valueOf(System.getProperty(PROPERTY_KEY_SELENIUM_BROWSER, browser.name()).toUpperCase());
	}

	private void initializeSeleniumApplicationRootUrl(SeleniumUrl applicationRootUrl) {
		String applicationRootHostName = System.getProperty(PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_HOSTNAME, applicationRootUrl.getHostName());
		SeleniumProtocolType applicationRootProtocol = SeleniumProtocolType.valueOf(System.getProperty(PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_PROTOCOL, applicationRootUrl.getProtocol().name())
				.toUpperCase());
		int applicationRootPort = Integer.parseInt(System.getProperty(PROPERTY_KEY_SELENIUM_APPLICATION_ROOT_PORT, "" + applicationRootUrl.getPort()));

		this.applicationRootUrl = new SeleniumUrl(applicationRootProtocol, applicationRootHostName, applicationRootPort).getUrl();
	}

	private void initializeSeleniumServerUrl(SeleniumUrl seleniumServerUrl) {
		String seleniumServerHostName = System.getProperty(PROPERTY_KEY_SELENIUM_SERVER_HOSTNAME, seleniumServerUrl.getHostName());
		SeleniumProtocolType seleniumServerProtocol = SeleniumProtocolType.valueOf(System.getProperty(PROPERTY_KEY_SELENIUM_SERVER_PROTOCOL, seleniumServerUrl.getProtocol().name()).toUpperCase());
		int seleniumServerPort = Integer.parseInt(System.getProperty(PROPERTY_KEY_SELENIUM_SERVER_PORT, "" + seleniumServerUrl.getPort()));
		String seleniumServerResource = System.getProperty(PROPERTY_KEY_SELENIUM_SERVER_RESOURCE, seleniumServerUrl.getResource());

		SeleniumUrl url = new SeleniumUrl(seleniumServerProtocol, seleniumServerHostName, seleniumServerPort, seleniumServerResource);

		this.seleniumServerUrl = url.getUrl();
	}

	URL getSeleniumServerUrl() {
		return this.seleniumServerUrl;
	}

	URL getApplicationRootUrl() {
		return this.applicationRootUrl;
	}

	SeleniumBrowserType getBrowser() {
		return this.browser;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder("SeleniumConfiguration: \n");
		result.append("seleniumServerUrl : ").append(this.seleniumServerUrl.toString()).append("\n");
		result.append("applocationRootUrl: ").append(this.applicationRootUrl.toString()).append("\n");
		result.append("browser           : ").append(this.browser.toString()).append("\n");
		return result.toString();
	}

	/**
	 * Diese Klasse stellt die Browsertypen bereit, welche von Selenium genutzt
	 * werden können.
	 */
	public enum SeleniumBrowserType {
		FIREFOX, INTERNET_EXPLORER
	}

}
