package mark.component.dbmodel.constans;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a named object.
 */
public interface NamedObject extends Serializable, Comparable<NamedObject> {

    /**
     * Gets an attribute.
     * 
     * @param name
     *        Attribute name.
     * @return Attribute value.
     */
    Object getAttribute(String name);

    /**
     * Gets an attribute.
     * 
     * @param name
     *        Attribute name.
     * @return Attribute value.
     */
     <T> T getAttribute(String name, T defaultValue);

    /**
     * Gets all attributes.
     * 
     * @return Map of attributes
     */
    Map<String, Object> getAttributes();

    /**
     * Getter for fully qualified name of object.
     * 
     * @return Fully qualified of the object
     */
    String getFullName();

    /**
     * A value guaranteed to be unique in the database for this object.
     */
    String getLookupKey();

    /**
     * Getter for name of object.
     * 
     * @return Name of the object
     */
    String getName();

    /**
     * Getter for remarks.
     * 
     * @return Remarks
     */
    String getRemarks();

    /**
     * Sets an attribute.
     * 
     * @param name
     *        Attribute name
     * @param value
     *        Attribute value
     */
    void setAttribute(String name, Object value);
}
