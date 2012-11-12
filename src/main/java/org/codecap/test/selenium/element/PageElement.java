package org.codecap.test.selenium.element;

/**
 * Diese Klasse repräsentiert Seitenelemente. Seitenelemente haben einen Zugriffsweg ({@link AccessType} und einen Wert, welcher
 * den Schlüssel darstellt, mit dem man auf dem angegebenen Zugriffsweg zu dem jeweiligen Seitenelement kommt.
 */
public class PageElement {

    /**
     * Der Schlüssel.
     */
    private final String key;

    /**
     * Der Zugriffsweg, welcher zum Element führt.
     */
    private AccessType accessType = AccessType.ID;

    /**
     * Optionaler Frame, in welchem das gesuchte Element zu finden ist. Frames müssen explizit "betreten" werden, um auf ihre Inhalte zugreifen zu können.
     */
    private Frame frame;

    /**
     * Der Konstruktor, welcher die initial notwendigen Werte entgegennimmt.
     * 
     * @param accessType
     *            Der Zugrisffsweg für das jeweilige Seitenelement.
     * @param key
     *            Der Schlüssel, welcher den Zugriffsweg definiert.
     */
    public PageElement(AccessType accessType, String key) {
        super();
        this.accessType = accessType;
        this.key = key;
    }

    /**
     * Der Konstruktor, welcher die initial notwendigen Werte entgegennimmt. ID ist hier der Default-Zugriffstyp
     * 
     * @param key
     *            Der Schlüssel, welcher den Zugriffsweg definiert.
     */
    public PageElement(String key) {
        super();
        this.key = key;
    }

    /**
     * Der Konstruktor, welcher die initial notwendigen Werte entgegennimmt.
     * 
     * @param frame
     *            The Frame to get the Element from.
     * @param accessType
     *            Der Zugrisffsweg für das jeweilige Seitenelement.
     * @param key
     *            Der Schlüssel, welcher den Zugriffsweg definiert.
     */
    public PageElement(Frame frame, AccessType accessType, String key) {
        super();
        this.frame = frame;
        this.accessType = accessType;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    Frame getFrame() {
        return frame;
    }

    boolean hasFrame() {
        return (frame != null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("PageInformation: ");
        if (frame != null) {
            builder.append("frame: ").append(frame.getKey()).append("; ");
        }
        builder.append("accessType: ").append(accessType).append("; ");
        builder.append("value: ").append(key).append("; ");
        return builder.toString();
    }
}
