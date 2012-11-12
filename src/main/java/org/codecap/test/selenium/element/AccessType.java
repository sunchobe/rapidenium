package org.codecap.test.selenium.element;

/**
 * Hier werden die Zugtriffstypen f�r HTML-Elemente definiert.
 */
public enum AccessType {

	/**
	 * Element-ID f�r die Navigation zu HTML-Elementen.
	 */
	ID,

	/**
	 * Zugriff auf HTML-Elemente via name ist etwas unsicherer, da nicht unbedingt eindeutig.
	 */
	NAME,

	/**
	 * Ansurfen von HTML-Elementen via XPATH.
	 */
	XPATH,

	/**
	 * FRAME stellt eine Sonderform dar, da Frames etwas anders in der Naviation und folglich auch in der Inhaltsanalyse funktionieren.
	 */
	FRAME,

}
