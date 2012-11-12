package org.codecap.test.selenium.element;

/**
 * Diese Klasse stellt einen Spezialfall von Seitenelementen dar, da explizit zu den inhalten navigiert werden muss.
 * siehe auch: {@link Page#enterFrame(Frame)}
 */
public class Frame extends PageElement {

	/**
	 * Konstruktor, um nur {@link AccessType#FRAME} für Frames zuzulassen.
	 * 
	 * @param key
	 *            Der Zugriffskey.
	 */
	public Frame(String key) {
		super(AccessType.FRAME, key);
	}

}
