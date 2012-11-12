package org.codecap.test.selenium;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Diese Klasse repräsentiert einen Wrapper für Urls, welche via Spring konfiguriert und von Selenium
 * bei dessen Konfiguration genutzt werden.
 */
public class SeleniumUrl {

	private static final Logger LOGGER = Logger.getLogger(SeleniumUrl.class);

	private final SeleniumProtocolType protocol;

	private final String host;

	private final int port;

	private String resource = "";

	/**
	 * Konstruktor, welche die Mindestanforderungen an Parametern übernimmt.
	 * 
	 * @param protocol
	 *            Das Protokoll.
	 * @param host
	 *            Der Host.
	 * @param port
	 *            Der Port.
	 */
	public SeleniumUrl(SeleniumProtocolType protocol, String host, int port) {
		super();

		this.protocol = protocol;
		this.host = host;
		this.port = port;
	}

	/**
	 * Konstruktor, welche die Mindestanforderungen an Parametern übernimmt.
	 * 
	 * @param protocol
	 *            Das Protokoll.
	 * @param host
	 *            Der Host.
	 * @param port
	 *            Der Port.
	 */
	public SeleniumUrl(SeleniumProtocolType protocol, String host, int port, String resource) {
		this(protocol, host, port);

		this.resource = resource;
	}

	String getHostName() {
		return host;
	}

	SeleniumProtocolType getProtocol() {
		return protocol;
	}

	int getPort() {
		return port;
	}

	String getResource() {
		return resource;
	}

	/**
	 * Erzeugt die {@link URL} mit den initialisierten Parametern.
	 * 
	 * @return Die {@link URL}.
	 */
	URL getUrl() {
		try {
			return new URL(protocol.name(), host, port, resource);
		} catch (MalformedURLException e) {
			StringBuffer message = new StringBuffer("Could not create URL for given parameters: ").append(toString());
			LOGGER.error(message, e);
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("Url");
		result.append("protocol = ").append(protocol).append("; ");
		result.append("host = ").append(host).append("; ");
		result.append("port = ").append(port).append("; ");
		result.append("resource = ").append(resource).append(";");
		return result.toString();
	}

	/**
	 * Hier werden die Protokoll-Typen hinterlegt, welche Selenium verwendet.
	 * 
	 * @author Christian Ober
	 */
	public enum SeleniumProtocolType {

		HTTP("http");

		private String protocolString;

		private SeleniumProtocolType(String protocolString) {
			this.protocolString = protocolString;
		}

		@Override
		public String toString() {
			return protocolString;
		}
	}
}
